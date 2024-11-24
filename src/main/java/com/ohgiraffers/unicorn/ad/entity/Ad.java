package com.ohgiraffers.unicorn.ad.entity;

import com.ohgiraffers.unicorn.BaseTimeEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Ad extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adId;

    @Column
    private Long corpId;

    @Column
    private String fileUrl;

    @Column
    private String type;

    @Column
    private String description;

    @Column
    private String adVideoUrl;

    @ColumnDefault("1")
    private Integer isOpened;

    public Ad() {}

    public Ad(Long corpId, String fileUrl, String type, String description, Integer isOpened) {
        this.corpId = corpId;
        this.fileUrl = fileUrl;
        this.type = type;
        this.description = description;
        this.isOpened = isOpened;
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

    public void setFileUrl(String mediaUrl) {
        this.fileUrl = mediaUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Integer isOpened) {
        this.isOpened = isOpened;
    }

    public String getAdVideoUrl() { return adVideoUrl; }

    public void setAdVideoUrl(String adVideoUrl) { this.adVideoUrl = adVideoUrl; }

    @Override
    public String toString() {
        return "Ad{" +
                "adId=" + adId +
                ", corpId='" + corpId + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", adVideoUrl='" + adVideoUrl + '\'' +
                ", isOpened=" + isOpened +
                '}';
    }
}
