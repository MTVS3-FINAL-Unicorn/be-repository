package com.ohgiraffers.unicorn.auth.controller;


import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.service.CorpService;
import com.ohgiraffers.unicorn.auth.service.IndivService;
import com.ohgiraffers.unicorn.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ohgiraffers.unicorn.utils.SecurityUtils.getCurrentUserId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/corp")
public class CorpController {

    private final CorpService corpService;

    /* 기업 회원 가입 */
    @PostMapping("/signup")
    public ResponseEntity<?> signUpCorporate(@Valid @RequestBody UserRequestDTO.CorporateSignUpDTO requestDTO) {
        corpService.corporateSignUp(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, @Valid @RequestBody UserRequestDTO.loginDTO requestDTO) {

        // 로그인 성공 DTO를 서비스로부터 가져옴
        UserResponseDTO.corpLoginSuccessDTO loginSuccessDTO = corpService.login(httpServletRequest, requestDTO);

        // 토큰과 사용자 정보를 포함한 응답 반환
        return ResponseEntity.ok()
                .body(ApiUtils.success(loginSuccessDTO));
    }

    /* 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        log.info("로그아웃 시도");

        corpService.logout();

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {

        UserResponseDTO.CorpProfileDTO responseDTO = corpService.getUserProfile(getCurrentUserId());

        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }
}
