package com.ohgiraffers.unicorn.space.dto;

import com.ohgiraffers.unicorn.space.entity.BrandSpace;

import java.util.List;

public class PaperingWrapper {
    private List<BrandSpace.Papering> papering;

    public List<BrandSpace.Papering> getPapering() {
        return papering;
    }

    public void setPapering(List<BrandSpace.Papering> papering) {
        this.papering = papering;
    }
}
