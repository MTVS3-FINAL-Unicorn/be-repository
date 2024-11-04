package com.ohgiraffers.unicorn.auth.controller;


import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.dto.UserResponseDTO;
import com.ohgiraffers.unicorn.auth.service.UserService;
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

    /* 개인 회원 가입 */
    @PostMapping("/signup/user")
    public ResponseEntity<?> signUpIndividual(@Valid @RequestBody UserRequestDTO.IndividualSignUpDTO requestDTO) {
        userService.signUpIndividual(requestDTO);
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
