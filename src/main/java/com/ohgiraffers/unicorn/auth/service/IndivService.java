package com.ohgiraffers.unicorn.auth.service;

import com.ohgiraffers.unicorn.auth.entity.Indiv;
import com.ohgiraffers.unicorn.error.exception.Exception401;
import com.ohgiraffers.unicorn.jwt.JWTTokenProvider;
import com.ohgiraffers.unicorn.auth.entity.Authority;
import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.repository.IndivRepository;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class IndivService {

    private final IndivRepository indivRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    /* 개인 회원 가입 */
    @Transactional
    public void individualSignUp(UserRequestDTO.IndividualSignUpDTO requestDTO) {

        LocalDate birthDate = LocalDate.parse(requestDTO.birthDate());

        Indiv indiv = createIndividualUser(requestDTO, birthDate);
        indivRepository.save(indiv);
    }

    // 로그인
    public UserResponseDTO.indivLoginSuccessDTO login(HttpServletRequest httpServletRequest, UserRequestDTO.loginDTO requestDTO) {
        Indiv indiv = findMemberByEmail(requestDTO.email())
                .orElseThrow(() -> new Exception401("해당 이메일은 회원 가입 되지 않은 이메일입니다."));
        UserResponseDTO.authTokenDTO authTokenDTO = getAuthTokenDTO(requestDTO.email(), requestDTO.password(), httpServletRequest);

        int age = calculateAge(indiv.getBirthDate());

        return new UserResponseDTO.indivLoginSuccessDTO(
                authTokenDTO.grantType(),
                authTokenDTO.accessToken(),
                authTokenDTO.accessTokenValidTime(),
                indiv.getId(),
                indiv.getName(),
                indiv.getNickname(),
                indiv.getContact(),
                indiv.getCategoryId(),
                age
        );
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    protected Optional<Indiv> findMemberByEmail(String email) {
        log.info("회원 확인 : {}", email);
        return indivRepository.findByEmail(email);
    }

    protected Indiv createIndividualUser(UserRequestDTO.IndividualSignUpDTO requestDTO, LocalDate birthDate) {
        return Indiv.builder()
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .name(requestDTO.name())
                .nickname(requestDTO.nickname())
                .birthDate(birthDate)
                .gender(requestDTO.gender())
                .contact(requestDTO.contact())
                .categoryId(requestDTO.categoryId())
                .authority(Authority.INDIV)
                .build();
    }

    protected UserResponseDTO.authTokenDTO getAuthTokenDTO(String email, String password, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public void logout() {
        log.info("로그아웃 - Refresh Token 확인");
    }

    public UserResponseDTO.IndivProfileDTO getUserProfile(Long currentUserId) {
        Indiv indiv = indivRepository.findById(currentUserId)
                .orElseThrow(() -> new Exception401("로그인부터 해주세요"));

        int age = calculateAge(indiv.getBirthDate());

        return new UserResponseDTO.IndivProfileDTO(
                indiv.getId(),
                indiv.getName(),
                indiv.getNickname(),
                age,
                indiv.getGender(),
                indiv.getContact(),
                indiv.getCategoryId()
                );
    }
}
