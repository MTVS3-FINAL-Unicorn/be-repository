package com.ohgiraffers.unicorn._core.utils;

import com.ohgiraffers.unicorn.auth.repository.CorpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomCorpDetail implements UserDetailsService {

    private final CorpRepository corpRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("__________"+email);
        return corpRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s은(는) 없는 이메일 입니다. 다시 확인해주세요.", email)));
    }

    public UserDetails createUserDetails(com.ohgiraffers.unicorn.auth.entity.Corp c) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(c.getAuthority().toString());

        System.out.println("grantedAuthority = " + grantedAuthority);
        System.out.println("c = " + c);

        return User.builder()
                .username(c.getId().toString())
                .password(c.getPassword())
                .authorities(grantedAuthority)
                .build();
    }

}
