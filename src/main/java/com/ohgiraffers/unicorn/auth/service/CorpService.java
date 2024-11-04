package com.ohgiraffers.unicorn.auth.service;

import com.ohgiraffers.unicorn.auth.dto.UserRequestDTO;
import com.ohgiraffers.unicorn.auth.entity.Authority;
import com.ohgiraffers.unicorn.auth.entity.Corp;
import com.ohgiraffers.unicorn.auth.repository.CorpRepository;
import com.ohgiraffers.unicorn.error.exception.Exception400;
import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import com.ohgiraffers.unicorn.space.repository.BrandSpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CorpService {

    private final BrandSpaceRepository brandSpaceRepository;
    private final PasswordEncoder passwordEncoder;
    private final CorpRepository corpRepository;

    /* 기업 회원 가입 */
    @Transactional
    public void signUpCorporate(UserRequestDTO.CorporateSignUpDTO requestDTO) {
        checkValidPassword(requestDTO.password(), passwordEncoder.encode(requestDTO.confirmPassword()));
        Corp corp = createCorporateUser(requestDTO);
        corpRepository.save(corp);
        createDefaultBrandSpaceForCorp(corp.getId());  // MongoDB에 기본 BrandSpace 엔티티 생성
    }

    // 기본 BrandSpace 생성
    private void createDefaultBrandSpaceForCorp(Long corpId) {
        BrandSpace defaultBrandSpace = new BrandSpace();
        defaultBrandSpace.setCorpId(corpId);
        defaultBrandSpace.setItems(List.of());
        defaultBrandSpace.setBgm(0);
        defaultBrandSpace.setLighting(new BrandSpace.Light());
        defaultBrandSpace.setQna(List.of());

        brandSpaceRepository.save(defaultBrandSpace);
    }

    private void checkValidPassword(String rawPassword, String encodedPassword) {
        log.info("{} {}", rawPassword, encodedPassword);
        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new Exception400("비밀번호가 일치하지 않습니다.");
        }
    }

    protected Corp createCorporateUser(UserRequestDTO.CorporateSignUpDTO requestDTO) {
        return Corp.builder()
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .brandName(requestDTO.brandName())
                .authority(Authority.CORP)
                .build();
    }

}
