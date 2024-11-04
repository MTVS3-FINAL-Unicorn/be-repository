package com.ohgiraffers.unicorn.auth.controller;


import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.service.AuthService;
import com.ohgiraffers.unicorn.auth.service.UserService;
import com.ohgiraffers.unicorn.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, @Valid @RequestBody UserRequestDTO.loginDTO requestDTO) {

        // 로그인 성공 DTO를 서비스로부터 가져옴
        UserResponseDTO.LoginSuccessDTO loginSuccessDTO = authService.login(httpServletRequest, requestDTO);

        // 토큰과 사용자 정보를 포함한 응답 반환
        return ResponseEntity.ok()
                .body(ApiUtils.success(loginSuccessDTO));
    }

    /* 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        log.info("로그아웃 시도");

        authService.logout();

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

}
