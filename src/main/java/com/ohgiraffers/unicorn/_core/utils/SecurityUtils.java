package com.ohgiraffers.unicorn._core.utils;

import com.ohgiraffers.unicorn.error.exception.Exception403;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("name = " + name);

        if(name.equals("anonymousUser")) {
            throw new Exception403("익명의 유저는 접근 권한이 없습니다.");
        }

        return Long.parseLong(name);
    }
}

