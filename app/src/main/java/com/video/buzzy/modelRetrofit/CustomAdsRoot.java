package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomAdsRoot {

    @SerializedName("customAd")
    private List<CustomAdItem> customAd;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<CustomAdItem> getCustomAd() {
        return customAd;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class CustomAdItem {

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("publisherName")
        private String publisherName;

        @SerializedName("publisherLogo")
        private String publisherLogo;

        @SerializedName("show")
        private boolean show;

        @SerializedName("description")
        private String description;

        @SerializedName("_id")
        private String id;

        @SerializedName("video")
        private String video;

        @SerializedName("type")
        private int type;

        @SerializedName("title")
        private String title;

        @SerializedName("url")
        private String url;


        @SerializedName("productImage")
        private String productImage;
        @SerializedName("productUrl")
        private String productUrl;
        @SerializedName("productTag")
        private String productTag;

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductUrl() {
            return productUrl;
        }

        public void setProductUrl(String productUrl) {
            this.productUrl = productUrl;
        }

        public String getProductTag() {
            return productTag;
        }

        public void setProductTag(String productTag) {
            this.productTag = productTag;
        }

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getCreatedAt() {
            return createdAt;
        }

        public String getPublisherName() {
            return publisherName;
        }

        public String getPublisherLogo() {
            return publisherLogo;
        }

        public boolean isShow() {
            return show;
        }

        public String getDescription() {
            return description;
        }

        public String getId() {
            return id;
        }

        public String getVideo() {
            return video;
        }

        public int getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}