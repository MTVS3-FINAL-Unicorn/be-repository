package com.ohgiraffers.unicorn.utils;

import com.ohgiraffers.unicorn.user.repository.UserRepository;
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
public class CustomUserDetail implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s은(는) 없는 이메일 입니다. 다시 확인해주세요.", email)));
    }

    public UserDetails createUserDetails(com.ohgiraffers.unicorn.user.domain.User u) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(u.getAuthority().toString());

        return User.builder()
                .username(u.getId().toString())
                .password(u.getPassword())
                .authorities(grantedAuthority)
                .build();
    }

}
