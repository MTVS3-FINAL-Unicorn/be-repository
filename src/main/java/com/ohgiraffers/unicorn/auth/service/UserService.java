package com.ohgiraffers.unicorn.auth.service;

import com.ohgiraffers.unicorn.error.exception.Exception400;
import com.ohgiraffers.unicorn.error.exception.Exception401;
import com.ohgiraffers.unicorn.jwt.JWTTokenProvider;
import com.ohgiraffers.unicorn.auth.entity.Authority;
import com.ohgiraffers.unicorn.auth.entity.Corp;
import com.ohgiraffers.unicorn.auth.entity.User;
import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.repository.CorpRepository;
import com.ohgiraffers.unicorn.auth.repository.UserRepository;
import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import com.ohgiraffers.unicorn.space.repository.BrandSpaceRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BrandSpaceRepository brandSpaceRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JWTTokenProvider jwtTokenProvider;
    private final CorpRepository corpRepository;

    /* 개인 회원 가입 */
    @Transactional
    public void signUpIndividual(UserRequestDTO.IndividualSignUpDTO requestDTO) {
        checkValidPassword(requestDTO.password(), passwordEncoder.encode(requestDTO.confirmPassword()));
        User user = createIndividualUser(requestDTO);
        userRepository.save(user);
    }

    /* 기업 회원 가입 */
    @Transactional
    public void signUpCorporate(UserRequestDTO.CorporateSignUpDTO requestDTO) {
        checkValidPassword(requestDTO.password(), passwordEncoder.encode(requestDTO.confirmPassword()));
        Corp corp = createCorporateUser(requestDTO);
        corpRepository.save(corp);
        createDefaultBrandSpaceForCorp(corp.getId());  // MongoDB에 기본 BrandSpace 엔티티 생성
    }

    // 로그인
    public UserResponseDTO.LoginSuccessDTO login(HttpServletRequest httpServletRequest, UserRequestDTO.loginDTO requestDTO) {
        User user = findMemberByEmail(requestDTO.email())
                .orElseThrow(() -> new Exception401("해당 이메일은 회원 가입 되지 않은 이메일입니다."));
        checkValidPassword(requestDTO.password(), user.getPassword());
        UserResponseDTO.authTokenDTO authTokenDTO = getAuthTokenDTO(requestDTO.email(), requestDTO.password(), httpServletRequest);
        return new UserResponseDTO.LoginSuccessDTO(
                authTokenDTO.grantType(),
                authTokenDTO.accessToken(),
                authTokenDTO.accessTokenValidTime(),
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getGender()
        );
    }

    // 기본 BrandSpace 생성
    private void createDefaultBrandSpaceForCorp(Long corpId) {
        BrandSpace defaultBrandSpace = new BrandSpace();
        defaultBrandSpace.setCorpId(corpId);
        defaultBrandSpace.setItems(List.of());
        defaultBrandSpace.setBgm(0);
        defaultBrandSpace.setLighting(new BrandSpace.Light());
        defaultBrandSpace.setQna(List.of());

        brandSpaceRepository.save(defaultBrandSpace);
    }

    private void checkValidPassword(String rawPassword, String encodedPassword) {
        log.info("{} {}", rawPassword, encodedPassword);
        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new Exception400("비밀번호가 일치하지 않습니다.");
        }
    }

    protected Optional<User> findMemberByEmail(String email) {
        log.info("회원 확인 : {}", email);
        return userRepository.findByEmail(email);
    }

    protected User createIndividualUser(UserRequestDTO.IndividualSignUpDTO requestDTO) {
        return User.builder()
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .name(requestDTO.name())
                .age(requestDTO.age())
                .gender(requestDTO.gender())
                .authority(Authority.USER)
                .build();
    }

    protected Corp createCorporateUser(UserRequestDTO.CorporateSignUpDTO requestDTO) {
        return Corp.builder()
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .brandName(requestDTO.brandName())
                .authority(Authority.CORP)
                .build();
    }

    protected UserResponseDTO.authTokenDTO getAuthTokenDTO(String email, String password, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        AuthenticationManager manager = authenticationManagerBuilder.getObject();
        Authentication authentication = manager.authenticate(usernamePasswordAuthenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public void logout() {
        log.info("로그아웃 - Refresh Token 확인");
    }

    public UserResponseDTO.UserProfileDTO getUserProfile(Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new Exception401("로그인부터 해주세요"));
        return new UserResponseDTO.UserProfileDTO(user.getName());
    }
}
