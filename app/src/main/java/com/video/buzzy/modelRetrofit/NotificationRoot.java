package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("notifications")
    private List<NotificationsItem> notifications;

    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public List<NotificationsItem> getNotifications() {
        return notifications;
    }

    public boolean isStatus() {
        return status;
    }

    public static class NotificationsItem {

        @SerializedName("date")
        private String date;

        @SerializedName("image")
        private Object image;

        @SerializedName("reelId")
        private String reelId;

        @SerializedName("name")
        private String name;

        @SerializedName("_id")
        private String id;

        @SerializedName("time")
        private String time;

        @SerializedName("message")
        private String message;

        @SerializedName("userId")
        private String userId;

        @SerializedName("otherUserId")
        private String otherUserId;

        public String getDate() {
            return date;
        }

        public Object getImage() {
            return image;
        }

        public String getReelId() {
            return reelId;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getTime() {
            return time;
        }

        public String getMessage() {
            return message;
        }

        public String getUserId() {
            return userId;
        }

        public String getOtherUserId() {
            return otherUserId;
        }
    }
}