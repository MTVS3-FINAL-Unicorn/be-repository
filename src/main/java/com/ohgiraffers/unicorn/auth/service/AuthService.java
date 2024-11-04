package com.ohgiraffers.unicorn.auth.service;

import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.entity.User;
import com.ohgiraffers.unicorn.auth.repository.UserRepository;
import com.ohgiraffers.unicorn.error.exception.Exception400;
import com.ohgiraffers.unicorn.error.exception.Exception401;
import com.ohgiraffers.unicorn.jwt.JWTTokenProvider;
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

import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JWTTokenProvider jwtTokenProvider;

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
