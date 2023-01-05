package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserFollowersRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private List<UserItem> user;

    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public List<UserItem> getUser() {
        return user;
    }

    public boolean isStatus() {
        return status;
    }

    public static class UserItem {

        @SerializedName("image")
        private Object image;

        @SerializedName("isFollow")
        private boolean isFollow;

        @SerializedName("gender")
        private Object gender;
        @SerializedName("name")
        private String name;
        @SerializedName("bio")
        private String bio;
        @SerializedName("userId")
        private String userId;
        @SerializedName("username")
        private String username;

        public void setFollow(boolean follow) {
            isFollow = follow;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }

        public boolean isIsFollow() {
            return isFollow;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
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