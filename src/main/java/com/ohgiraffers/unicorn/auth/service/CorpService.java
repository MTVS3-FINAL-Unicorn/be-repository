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
    public void corporateSignUp(UserRequestDTO.CorporateSignUpDTO requestDTO) {
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

    protected Corp createCorporateUser(UserRequestDTO.CorporateSignUpDTO requestDTO) {
        return Corp.builder()
                .brandName(requestDTO.brandName())
                .picName(requestDTO.picName())
                .binNo(requestDTO.binNo())
                .contact(requestDTO.contact())
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .authority(Authority.CORP)
                .build();
    }

}
