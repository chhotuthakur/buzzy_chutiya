package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReelsVideoRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("reel")
    private Reel reel;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public Reel getReel() {
        return reel;
    }

    public static class Reel {

        @SerializedName("song")
        private String song;

        @SerializedName("date")
        private String date;

        @SerializedName("thumbnail")
        private Object thumbnail;

        @SerializedName("like")
        private int like;

        @SerializedName("isDelete")
        private boolean isDelete;

        @SerializedName("isProductShow")
        private boolean isProductShow;

        @SerializedName("caption")
        private Object caption;

        @SerializedName("video")
        private String video;

        @SerializedName("screenshot")
        private Object screenshot;

        @SerializedName("mentionPeople")
        private List<String> mentionPeople;

        @SerializedName("userId")
        private String userId;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("productImage")
        private Object productImage;

        @SerializedName("productTag")
        private String productTag;

        @SerializedName("__v")
        private int V;

        @SerializedName("comment")
        private int comment;

        @SerializedName("location")
        private String location;

        @SerializedName("_id")
        private String id;

        @SerializedName("productUrl")
        private String productUrl;

        @SerializedName("hashtag")
        private List<String> hashtag;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getSong() {
            return song;
        }

        public String getDate() {
            return date;
        }

        public Object getThumbnail() {
            return thumbnail;
        }

        public int getLike() {
            return like;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public boolean isIsProductShow() {
            return isProductShow;
        }

        public Object getCaption() {
            return caption;
        }

        public String getVideo() {
            return video;
        }

        public Object getScreenshot() {
            return screenshot;
        }

        public List<String> getMentionPeople() {
            return mentionPeople;
        }

        public String getUserId() {
            return userId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public Object getProductImage() {
            return productImage;
        }

        public String getProductTag() {
            return productTag;
        }

        public int getV() {
            return V;
        }

        public int getComment() {
            return comment;
        }

        public String getLocation() {
            return location;
        }

        public String getId() {
            return id;
        }

        public String getProductUrl() {
            return productUrl;
        }

        public List<String> getHashtag() {
            return hashtag;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}