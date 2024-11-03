package com.ohgiraffers.unicorn.user.controller;


import com.ohgiraffers.unicorn.user.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.user.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.user.service.UserService;
import com.ohgiraffers.unicorn.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    /* 회원가입 */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserRequestDTO.signUpDTO requestDTO) {

        userService.signUp(requestDTO);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest, @Valid @RequestBody UserRequestDTO.loginDTO requestDTO) {

        // 로그인 성공 DTO를 서비스로부터 가져옴
        UserResponseDTO.LoginSuccessDTO loginSuccessDTO = userService.login(httpServletRequest, requestDTO);

        // 토큰과 사용자 정보를 포함한 응답 반환
        return ResponseEntity.ok()
                .body(ApiUtils.success(loginSuccessDTO));
    }

    /* 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        log.info("로그아웃 시도");

        userService.logout();

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

//    @GetMapping("/profile")
//    public ResponseEntity<?> getUserProfile() {
//
//        UserResponseDTO.UserProfileDTO responseDTO = userService.getUserProfile(getCurrentUserId());
//
//        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
//    }
}
