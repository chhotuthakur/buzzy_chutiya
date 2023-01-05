package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatRoot {

    @SerializedName("chat")
    private List<ChatItem> chat;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<ChatItem> getChat() {
        return chat;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class ChatItem {

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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @SerializedName("time")
        private String time;

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        @SerializedName("giftId")
        private String giftId;

        @SerializedName("_id")
        private String id;

        @Override
        public String toString() {
            return "ChatItem{" +
                    "date='" + date + '\'' +
                    ", image='" + image + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", senderId='" + senderId + '\'' +
                    ", messageType=" + messageType +
                    ", topic='" + topic + '\'' +
                    ", id='" + id + '\'' +
                    ", message='" + message + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    '}';
        }

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