package com.ohgiraffers.unicorn.ad.entity;

import com.ohgiraffers.unicorn.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Ad extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adId;

    @Column(nullable = false)
    private Long corpId;

    @Column
    private String fileUrl;

    @Column
    private String previewUrl;

    @Column
    private String description;

    @Column
    private String adVideoUrl;

    public Ad() {}

    public Ad(Long corpId, String fileUrl, String previewUrl, String description) {
        this.corpId = corpId;
        this.fileUrl = fileUrl;
        this.previewUrl = previewUrl;
        this.description = description;
    }

    public Ad(Long corpId, String fileUrl, String description) {
        this.corpId = corpId;
        this.fileUrl = fileUrl;
        this.description = description;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public Long getCorpId() {
        return corpId;
    }

    public void setCorpId(Long corpId) {
        this.corpId = corpId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdVideoUrl() {
        return adVideoUrl;
    }

    public void setAdVideoUrl(String adVideoUrl) {
        this.adVideoUrl = adVideoUrl;
    }
}
