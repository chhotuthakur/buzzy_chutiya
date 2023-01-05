package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

public class CommentPostRoot {

    @SerializedName("comment")
    private Comment comment;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public Comment getComment() {
        return comment;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class Comment {

        @SerializedName("image")
        private String image;

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

        public String getImage() {
            return image;
        }

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public String getId() {
            return id;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public String getTime() {
            return time;
        }

        public String getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }
}