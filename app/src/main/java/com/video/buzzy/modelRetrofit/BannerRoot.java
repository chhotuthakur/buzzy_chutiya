package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerRoot {

    @SerializedName("banner")
    private List<BannerItem> banner;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<BannerItem> getBanner() {
        return banner;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class BannerItem {

        @SerializedName("image")
        private String image;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("_id")
        private String id;

        @SerializedName("isVIP")
        private boolean isVIP;

        @SerializedName("URL")
        private String uRL;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getImage() {
            return image;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getId() {
            return id;
        }

        public boolean isIsVIP() {
            return isVIP;
        }

        public String getURL() {
            return uRL;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}