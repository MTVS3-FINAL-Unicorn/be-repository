package com.ohgiraffers.unicorn.auth.service;

import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.Authority;
import com.ohgiraffers.unicorn.auth.entity.Corp;
import com.ohgiraffers.unicorn.auth.repository.CorpRepository;
import com.ohgiraffers.unicorn.error.exception.Exception401;
import com.ohgiraffers.unicorn.jwt.JWTTokenProvider;
import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import com.ohgiraffers.unicorn.space.repository.BrandSpaceRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CorpService {

    private final BrandSpaceRepository brandSpaceRepository;
    private final PasswordEncoder passwordEncoder;
    private final CorpRepository corpRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    /* 기업 회원 가입 */
    @Transactional
    public void corporateSignUp(UserRequestDTO.CorporateSignUpDTO requestDTO) {
        Corp corp = createCorporateUser(requestDTO);
        corpRepository.save(corp);
        createDefaultBrandSpaceForCorp(corp.getId());  // MongoDB에 기본 BrandSpace 엔티티 생성
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

    protected Corp createCorporateUser(UserRequestDTO.CorporateSignUpDTO requestDTO) {
        return Corp.builder()
                .brandName(requestDTO.brandName())
                .picName(requestDTO.picName())
                .binNo(requestDTO.binNo())
                .contact(requestDTO.contact())
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .authority(Authority.CORP)
                .build();
    }

    public UserResponseDTO.corpLoginSuccessDTO login(HttpServletRequest httpServletRequest, UserRequestDTO.loginDTO requestDTO) {
        Corp corp = findMemberByEmail(requestDTO.email())
                .orElseThrow(() -> new Exception401("해당 이메일은 회원 가입 되지 않은 이메일입니다."));

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(requestDTO.email(), requestDTO.password());
        Authentication authentication = authenticationManager.authenticate(authRequest);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserResponseDTO.authTokenDTO authTokenDTO = getAuthTokenDTO(requestDTO.email(), requestDTO.password(), httpServletRequest);

        return new UserResponseDTO.corpLoginSuccessDTO(
                authTokenDTO.grantType(),
                authTokenDTO.accessToken(),
                authTokenDTO.accessTokenValidTime(),
                corp.getId(),
                corp.getBrandName(),
                corp.getPicName(),
                corp.getBinNo(),
                corp.getContact()
        );
    }

    protected Optional<Corp> findMemberByEmail(String email) {
        log.info("회원 확인 : {}", email);
        return corpRepository.findByEmail(email);
    }

    protected UserResponseDTO.authTokenDTO getAuthTokenDTO(String email, String password, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public void logout() {
        log.info("로그아웃 - Refresh Token 확인");
    }

    public UserResponseDTO.CorpProfileDTO getUserProfile(Long currentUserId) {

        Corp corp = corpRepository.findById(currentUserId)
                .orElseThrow(() -> new Exception401("로그인부터 해주세요"));

        return new UserResponseDTO.CorpProfileDTO(
                corp.getBrandName(),
                corp.getPicName(),
                corp.getBinNo(),
                corp.getContact()
        );
    }

}
