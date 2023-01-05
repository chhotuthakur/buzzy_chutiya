package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftRoot {

    @SerializedName("gift")
    private List<GiftItem> gift;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    @Override
    public String toString() {
        return "GiftRoot{" +
                "gift=" + gift +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    public List<GiftItem> getGift() {
        return gift;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class GiftItem {

        @SerializedName("image")
        private String image;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("isDelete")
        private boolean isDelete;

        @SerializedName("_id")
        private String id;

        @SerializedName("type")
        private int type;

        @SerializedName("coin")
        private int coin;

        @SerializedName("updatedAt")
        private String updatedAt;

        private int receivedcount = 0;

        public int getReceivedcount() {
            return receivedcount;
        }

        public void setReceivedcount(int receivedcount) {
            this.receivedcount = receivedcount;
        }

        public String getImage() {
            return image;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public String getId() {
            return id;
        }

        public int getType() {
            return type;
        }

        public int getCoin() {
            return coin;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}