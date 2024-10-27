package com.ohgiraffers.unicorn.space.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "brand_space")
public class BrandSpace {

    @Id
    private String id;
    private Long corpId;
    private List<Item> items;
    private int bgm;

    public BrandSpace(Long corpId, List<Item> items, int bgm) {
        this.corpId = corpId;
        this.items = items;
        this.bgm = bgm;
    }

    public String getId() {
        return id;
    }

    public Long getCorpId() {
        return corpId;
    }

    public void setCorpId(Long corpId) {
        this.corpId = corpId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getBgm() {
        return bgm;
    }

    public void setBgm(int bgm) {
        this.bgm = bgm;
    }

    @Override
    public String toString() {
        return "BrandSpace{" +
                "id='" + id + '\'' +
                ", corpId=" + corpId +
                ", items=" + items +
                ", bgm=" + bgm +
                '}';
    }

    public static class Item {
        private Long itemId;
        private Transform transform;

        public static class Transform {
            private String location;
            private String rotation;
            private String scale;

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getRotation() {
                return rotation;
            }

            public void setRotation(String rotation) {
                this.rotation = rotation;
            }

            public String getScale() {
                return scale;
            }

            public void setScale(String scale) {
                this.scale = scale;
            }

            @Override
            public String toString() {
                return "Transform{" +
                        "location='" + location + '\'' +
                        ", rotation='" + rotation + '\'' +
                        ", scale='" + scale + '\'' +
                        '}';
            }
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public Transform getTransform() {
            return transform;
        }

        public void setTransform(Transform transform) {
            this.transform = transform;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "itemId=" + itemId +
                    ", transform=" + transform +
                    '}';
        }
    }
}