package com.ohgiraffers.unicorn.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRequestDTO {

    // 회원 가입
    public record signUpDTO(
            @Email(message = "올바른 이메일 주소를 입력해 주세요.")
            @NotBlank(message = "이메일을 입력해 주세요.")
            String email,
            @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "패스워드는 최소 6자 이상이어야 하며, 숫자를 포함해야 합니다.")
            String password,
            String confirmPassword,
            @NotBlank(message = "성함을 입력해 주세요.")
            String name,
            @NotBlank(message = "연령을 선택해 주세요.")
            int age,
            @NotBlank(message = "성별을 선택해 주세요.")
            String gender
    ) {
    }

    // 로그인
    public record loginDTO(
            @Email(message = "올바른 이메일 주소를 입력해 주세요.")
            @NotBlank(message = "이메일을 입력해 주세요.")
            String email,
            String password
    ) {
    }

}
