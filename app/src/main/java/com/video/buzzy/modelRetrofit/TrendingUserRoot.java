package com.video.buzzy.modelRetrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendingUserRoot {

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

    public static class UserItem implements Parcelable {

        public static final Creator<UserItem> CREATOR = new Creator<UserItem>() {
            @Override
            public String toString() {
                return "$classname{}";
            }

            @Override
            public UserItem createFromParcel(Parcel in) {
                return new UserItem(in);
            }

            @Override
            public UserItem[] newArray(int size) {
                return new UserItem[size];
            }
        };
        @SerializedName("view")
        private int view;
        @SerializedName("gender")
        private Object gender;

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @SerializedName("like")
        private int like;
        @SerializedName("name")
        private String name;
        @SerializedName("bio")
        private String bio;
        @SerializedName("profileImage")
        private String profileImage;
        @SerializedName("userId")
        private String userId;
        @SerializedName("username")
        private String username;

        protected UserItem(Parcel in) {
            view = in.readInt();
            like = in.readInt();
            name = in.readString();
            bio = in.readString();
            profileImage = in.readString();
            userId = in.readString();
            username = in.readString();
        }

        public int getView() {
            return view;
        }

        public Object getGender() {
            return gender;
        }

        public int getLike() {
            return like;
        }

        public String getName() {
            return name;
        }

        public String getBio() {
            return bio;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public String getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(view);
            dest.writeInt(like);
            dest.writeString(name);
            dest.writeString(bio);
            dest.writeString(profileImage);
            dest.writeString(userId);
            dest.writeString(username);
        }
    }
}