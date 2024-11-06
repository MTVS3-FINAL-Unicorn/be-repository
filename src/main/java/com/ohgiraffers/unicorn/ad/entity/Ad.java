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
    private String mediaUrl;

    @Column
    private String type;

    @Column
    private String description;

    @ColumnDefault("1")
    private Integer isOpened;

    public Ad() {}

    public Ad(Long corpId, String mediaUrl, String type, String description) {
        this.corpId = corpId;
        this.mediaUrl = mediaUrl;
        this.type = type;
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

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
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

    @Override
    public String toString() {
        return "Ad{" +
                "adId=" + adId +
                ", corpId='" + corpId + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", isOpened=" + isOpened +
                '}';
    }
}
