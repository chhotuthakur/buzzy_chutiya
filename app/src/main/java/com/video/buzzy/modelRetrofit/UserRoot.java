package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public boolean isStatus() {
        return status;
    }

    public static class User {

        @SerializedName("gift")
        private List<GiftItem> gift;

        @SerializedName("lastLogin")
        private String lastLogin;

        @SerializedName("isBlock")
        private boolean isBlock;

        @SerializedName("loginType")
        private int loginType;

        @SerializedName("bio")
        private String bio;

        @SerializedName("isOnline")
        private boolean isOnline;

        @SerializedName("profileImage")
        private String profileImage;

        @SerializedName("view")
        private int view;

        @SerializedName("analyticDate")
        private String analyticDate;

        @SerializedName("identity")
        private String identity;

        @SerializedName("coverImage")
        private String coverImage;

        @SerializedName("fcm_token")
        private String fcmToken;

        @SerializedName("email")
        private String email;

        @SerializedName("notification")
        private boolean notification;

        public boolean getNotification() {
            return notification;
        }

        public void setNotification(boolean notification) {
            this.notification = notification;
        }

        @SerializedName("ad")
        private Ad ad;

        @SerializedName("like")
        private int like;

        @SerializedName("requestForWithdrawDiamond")
        private int requestForWithdrawDiamond;

        @SerializedName("reels")
        private int reels;

        @SerializedName("reel")
        private List<ReelItem> reel;

        @SerializedName("diamond")
        private int diamond;

        @SerializedName("followers")
        private int followers;

        @SerializedName("following")
        private int following;

        @SerializedName("name")
        private String name;

        @SerializedName("comment")
        private int comment;

        @SerializedName("_id")
        private String id;

        @SerializedName("coin")
        private int coin;

        @SerializedName("username")
        private String username;

        @SerializedName("isFollow")
        private boolean isFollow;

        public boolean isFollow() {
            return isFollow;
        }

        public void setFollow(boolean follow) {
            isFollow = follow;
        }

        public List<GiftItem> getGift() {
            return gift;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public boolean isIsBlock() {
            return isBlock;
        }

        public int getLoginType() {
            return loginType;
        }

        public String getBio() {
            return bio;
        }

        public boolean isIsOnline() {
            return isOnline;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public int getView() {
            return view;
        }

        public String getAnalyticDate() {
            return analyticDate;
        }

        public String getIdentity() {
            return identity;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public String getFcmToken() {
            return fcmToken;
        }

        public String getEmail() {
            return email;
        }

        public Ad getAd() {
            return ad;
        }

        public int getLike() {
            return like;
        }

        public int getRequestForWithdrawDiamond() {
            return requestForWithdrawDiamond;
        }

        public int getReels() {
            return reels;
        }

        public List<ReelItem> getReel() {
            return reel;
        }

        public int getDiamond() {
            return diamond;
        }

        public int getFollowers() {
            return followers;
        }

        public int getFollowing() {
            return following;
        }

        public String getName() {
            return name;
        }

        public int getComment() {
            return comment;
        }

        public String getId() {
            return id;
        }

        public int getCoin() {
            return coin;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class ReelItem {

        @SerializedName("song")
        private String song;

        @SerializedName("date")
        private String date;

        @SerializedName("thumbnail")
        private String thumbnail;

        @SerializedName("like")
        private int like;

        @SerializedName("isDelete")
        private boolean isDelete;

        @SerializedName("isProductShow")
        private boolean isProductShow;

        @SerializedName("caption")
        private String caption;

        @SerializedName("video")
        private String video;

        @SerializedName("screenshot")
        private String screenshot;

        @SerializedName("userId")
        private String userId;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("productImage")
        private String productImage;

        @SerializedName("productTag")
        private String productTag;

        @SerializedName("__v")
        private int V;

        @SerializedName("comment")
        private int comment;

        @SerializedName("_id")
        private String id;

        @SerializedName("productUrl")
        private String productUrl;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getSong() {
            return song;
        }

        public String getDate() {
            return date;
        }

        public String getThumbnail() {
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

        public String getCaption() {
            return caption;
        }

        public String getVideo() {
            return video;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public String getUserId() {
            return userId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getProductImage() {
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

        public String getId() {
            return id;
        }

        public String getProductUrl() {
            return productUrl;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }

    public static class GiftItem {

        @SerializedName("gift")
        private String gift;

        @SerializedName("count")
        private int count;

        @SerializedName("_id")
        private String id;

        public String getGift() {
            return gift;
        }

        public int getCount() {
            return count;
        }

        public String getId() {
            return id;
        }
    }

    public static class Ad {

        @SerializedName("date")
        private String date;

        @SerializedName("count")
        private int count;

        public String getDate() {
            return date;
        }

        public int getCount() {
            return count;
        }
    }
}