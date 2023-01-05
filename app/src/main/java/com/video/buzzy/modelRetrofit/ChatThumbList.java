package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatThumbList {


    @SerializedName("chatList")
    private List<Chat> chat;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<Chat> getChat() {
        return chat;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class Chat {
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("_id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("userId")
        private String userId;
        @SerializedName("userImage")
        private String userImage;
        @SerializedName("isOnline")
        private boolean isOnline;
        @SerializedName("date")
        private String date;
        @SerializedName("message")
        private String lastMsg;
        @SerializedName("time")
        private String time;
        @SerializedName("updatedAt")
        private String updatedAt;

        public String getId() {
            return id;
        }

        public String getLastMsg() {
            return lastMsg;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public boolean isOnline() {
            return isOnline;
        }

        public void setOnline(boolean online) {
            isOnline = online;
        }


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

    }


}