package com.ohgiraffers.unicorn.auth.controller;


import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.service.CorpService;
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
public class CorpController {

    private final CorpService corpService;

    /* 기업 회원 가입 */
    @PostMapping("/signup/corp")
    public ResponseEntity<?> signUpCorporate(@Valid @RequestBody UserRequestDTO.CorporateSignUpDTO requestDTO) {
        corpService.corporateSignUp(requestDTO);
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
