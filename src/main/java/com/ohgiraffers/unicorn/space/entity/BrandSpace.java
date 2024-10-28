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
    private Light lighting;
    private List<Qna> qna;

    public BrandSpace() {}

    public BrandSpace(Long corpId, List<Item> items, int bgm, Light lighting, List<Qna> qna) {
        this.corpId = corpId;
        this.items = items;
        this.bgm = bgm;
        this.lighting = lighting;
        this.qna = qna;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Light getLighting() {
        return lighting;
    }

    public void setLighting(Light lighting) {
        this.lighting = lighting;
    }

    public List<Qna> getQna() {
        return qna;
    }

    public void setQna(List<Qna> qna) {
        this.qna = qna;
    }

    @Override
    public String toString() {
        return "BrandSpace{" +
                "id='" + id + '\'' +
                ", corpId=" + corpId +
                ", items=" + items +
                ", bgm=" + bgm +
                ", lighting=" + lighting +
                ", qna=" + qna +
                '}';
    }

    public static class Item {
        private Long itemId;
        private Transform transform;

        public Item() {}

        public Item(Long itemId, Transform transform) {
            this.itemId = itemId;
            this.transform = transform;
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

        public static class Transform {
            private Location location;
            private Rotation rotation;
            private Scale scale;

            public Transform() {}

            public Transform(Location location, Rotation rotation, Scale scale) {
                this.location = location;
                this.rotation = rotation;
                this.scale = scale;
            }

            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }

            public Rotation getRotation() {
                return rotation;
            }

            public void setRotation(Rotation rotation) {
                this.rotation = rotation;
            }

            public Scale getScale() {
                return scale;
            }

            public void setScale(Scale scale) {
                this.scale = scale;
            }

            @Override
            public String toString() {
                return "Transform{" +
                        "location=" + location +
                        ", rotation=" + rotation +
                        ", scale=" + scale +
                        '}';
            }

            public static class Location {
                private double x;
                private double y;
                private double z;

                public Location() {}

                public Location(double x, double y, double z) {
                    this.x = x;
                    this.y = y;
                    this.z = z;
                }

                public double getX() {
                    return x;
                }

                public void setX(double x) {
                    this.x = x;
                }

                public double getY() {
                    return y;
                }

                public void setY(double y) {
                    this.y = y;
                }

                public double getZ() {
                    return z;
                }

                public void setZ(double z) {
                    this.z = z;
                }

                @Override
                public String toString() {
                    return "Location{" +
                            "x=" + x +
                            ", y=" + y +
                            ", z=" + z +
                            '}';
                }
            }

            public static class Rotation {
                private double pitch;
                private double yaw;
                private double roll;

                public Rotation() {}

                public Rotation(double pitch, double yaw, double roll) {
                    this.pitch = pitch;
                    this.yaw = yaw;
                    this.roll = roll;
                }

                public double getPitch() {
                    return pitch;
                }

                public void setPitch(double pitch) {
                    this.pitch = pitch;
                }

                public double getYaw() {
                    return yaw;
                }

                public void setYaw(double yaw) {
                    this.yaw = yaw;
                }

                public double getRoll() {
                    return roll;
                }

                public void setRoll(double roll) {
                    this.roll = roll;
                }

                @Override
                public String toString() {
                    return "Rotation{" +
                            "pitch=" + pitch +
                            ", yaw=" + yaw +
                            ", roll=" + roll +
                            '}';
                }
            }

            public static class Scale {
                private double x;
                private double y;
                private double z;

                public Scale() {}

                public Scale(double x, double y, double z) {
                    this.x = x;
                    this.y = y;
                    this.z = z;
                }

                public double getX() {
                    return x;
                }

                public void setX(double x) {
                    this.x = x;
                }

                public double getY() {
                    return y;
                }

                public void setY(double y) {
                    this.y = y;
                }

                public double getZ() {
                    return z;
                }

                public void setZ(double z) {
                    this.z = z;
                }

                @Override
                public String toString() {
                    return "Scale{" +
                            "x=" + x +
                            ", y=" + y +
                            ", z=" + z +
                            '}';
                }
            }
        }
    }

    public static class Light {
        private String color;
        private double intensity;

        public Light() {}

        public Light(String color, double intensity) {
            this.color = color;
            this.intensity = intensity;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public double getIntensity() {
            return intensity;
        }

        public void setIntensity(double intensity) {
            this.intensity = intensity;
        }

        @Override
        public String toString() {
            return "Light{" +
                    "color='" + color + '\'' +
                    ", intensity=" + intensity +
                    '}';
        }
    }

    public static class Qna {
        private String question;
        private String answer;

        public Qna() {}

        public Qna(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        @Override
        public String toString() {
            return "Qna{" +
                    "question='" + question + '\'' +
                    ", answer='" + answer + '\'' +
                    '}';
        }
    }
}
