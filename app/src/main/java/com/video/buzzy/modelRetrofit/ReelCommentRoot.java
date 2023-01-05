package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReelCommentRoot {

    @SerializedName("comment")
    private List<CommentItem> comment;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<CommentItem> getComment() {
        return comment;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class CommentItem {

        @SerializedName("image")
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @SerializedName("name")
        private String name;

        @SerializedName("comment")
        private String comment;

        @SerializedName("_id")
        private String id;

        @SerializedName("screenshot")
        private String screenshot;

        @SerializedName("time")
        private String time;

        @SerializedName("userId")
        private String userId;

        @SerializedName("username")
        private String username;

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}