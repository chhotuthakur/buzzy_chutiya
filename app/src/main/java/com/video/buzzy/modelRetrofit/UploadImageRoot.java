package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

public class UploadImageRoot {

    @SerializedName("chat")
    private Chat chat;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public Chat getChat() {
        return chat;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class Chat {

        @SerializedName("date")
        private String date;

        @SerializedName("image")
        private String image;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("senderId")
        private String senderId;

        @SerializedName("messageType")
        private int messageType;

        @SerializedName("topic")
        private String topic;

        @SerializedName("_id")
        private String id;

        @SerializedName("message")
        private String message;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getDate() {
            return date;
        }

        public String getImage() {
            return image;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getSenderId() {
            return senderId;
        }

        public int getMessageType() {
            return messageType;
        }

        public String getTopic() {
            return topic;
        }

        public String getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}