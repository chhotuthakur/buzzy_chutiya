package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedeemDiamondRoot {

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

    public static class ReelItem {

        @SerializedName("date")
        private String date;

        @SerializedName("isProductShow")
        private boolean isProductShow;

        @SerializedName("caption")
        private Object caption;

        @SerializedName("video")
        private String video;

        @SerializedName("screenshot")
        private String screenshot;

        @SerializedName("mentionPeople")
        private List<String> mentionPeople;

        @SerializedName("duration")
        private int duration;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("isOriginalAudio")
        private boolean isOriginalAudio;

        @SerializedName("productImage")
        private Object productImage;

        @SerializedName("productTag")
        private String productTag;

        @SerializedName("__v")
        private int V;

        @SerializedName("hashtag")
        private List<String> hashtag;

        @SerializedName("updatedAt")
        private String updatedAt;

        @SerializedName("song")
        private Object song;

        @SerializedName("thumbnail")
        private String thumbnail;

        @SerializedName("like")
        private int like;

        @SerializedName("isDelete")
        private boolean isDelete;

        @SerializedName("allowComment")
        private boolean allowComment;

        @SerializedName("userId")
        private String userId;

        @SerializedName("size")
        private String size;

        @SerializedName("showVideo")
        private int showVideo;

        @SerializedName("comment")
        private int comment;

        @SerializedName("location")
        private String location;

        @SerializedName("_id")
        private String id;

        @SerializedName("productUrl")
        private Object productUrl;

        public String getDate() {
            return date;
        }

        public boolean isIsProductShow() {
            return isProductShow;
        }

        public Object getCaption() {
            return caption;
        }

        public String getVideo() {
            return video;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public List<String> getMentionPeople() {
            return mentionPeople;
        }

        public int getDuration() {
            return duration;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public boolean isIsOriginalAudio() {
            return isOriginalAudio;
        }

        public Object getProductImage() {
            return productImage;
        }

        public String getProductTag() {
            return productTag;
        }

        public int getV() {
            return V;
        }

        public List<String> getHashtag() {
            return hashtag;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public Object getSong() {
            return song;
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

        public boolean isAllowComment() {
            return allowComment;
        }

        public String getUserId() {
            return userId;
        }

        public String getSize() {
            return size;
        }

        public int getShowVideo() {
            return showVideo;
        }

        public int getComment() {
            return comment;
        }

        public String getLocation() {
            return location;
        }

        public String getId() {
            return id;
        }

        public Object getProductUrl() {
            return productUrl;
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

        @SerializedName("username")
        private String username;

        @SerializedName("coin")
        private int coin;

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

        public String getUsername() {
            return username;
        }

        public int getCoin() {
            return coin;
        }
    }
}