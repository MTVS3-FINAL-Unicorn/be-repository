package com.ohgiraffers.unicorn.auth.dto;

import java.util.List;

public class UserResponseDTO {

    // 토큰 발급
    public record authTokenDTO(
            String grantType,
            String accessToken,
            Long accessTokenValidTime
    ) {
    }

    // 기업 회원 로그인 성공 시 반환하는 DTO
    public record corpLoginSuccessDTO(
            String grantType,
            String accessToken,
            Long accessTokenValidTime,
            Long corpId,
            String brandName,
            String picName,
            String binNo,
            String contact,
            int categoryId
    ) {
    }

    // 개인 회원 로그인 성공 시 반환하는 DTO
    public record indivLoginSuccessDTO(
            String grantType,
            String accessToken,
            Long accessTokenValidTime,
            Long indivId,
            String name,
            String nickname,
            String contact,
            List<Integer> categoryId,
            int age
    ) {
    }

    // getUSerProfile
    public record CorpProfileDTO(
            Long corpId,
            String brandName,
            String picName,
            String binNo,
            String contact,
            int categoryId
    ) {
    }

    public record IndivProfileDTO(
            Long indivId,
            String name,
            String nickname,
            int age,
            String gender,
            String contact,
            List<Integer> categoryId
    ) {
    }
}
