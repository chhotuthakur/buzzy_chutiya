package com.video.buzzy.modelRetrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllVideoRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("reel")
    private List<ReelItem> reel;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public List<ReelItem> getReel() {
        return reel;
    }

    public static class Song {

        @SerializedName("song")
        private String song;

        @SerializedName("image")
        private String image;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("singer")
        private String singer;

        @SerializedName("isDelete")
        private boolean isDelete;

        @SerializedName("_id")
        private String id;

        @SerializedName("title")
        private String title;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getSong() {
            return song;
        }

        public String getImage() {
            return image;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getSinger() {
            return singer;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getUpdatedAt() {
            return updatedAt;
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

        @SerializedName("gender")
        private String gender;

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

        public String getGender() {
            return gender;
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

    public static class ReelItem implements Parcelable {
        public static final Creator<ReelItem> CREATOR = new Creator<ReelItem>() {
            @Override
            public ReelItem createFromParcel(Parcel in) {
                return new ReelItem(in);
            }

            @Override
            public ReelItem[] newArray(int size) {
                return new ReelItem[size];
            }
        };

        @SerializedName("date")
        private String date;
        @SerializedName("isLike")
        private boolean isLike;
        @SerializedName("isProductShow")
        private boolean isProductShow;
        @SerializedName("caption")
        private String caption;
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
        private String productImage;
        @SerializedName("analyticDate")
        private String analyticDate;
        @SerializedName("productTag")
        private String productTag;
        @SerializedName("__v")
        private int V;
        @SerializedName("hashtag")
        private List<String> hashtag;
        @SerializedName("updatedAt")
        private String updatedAt;
        @SerializedName("song")
        private Song song;
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
        private String productUrl;
        @SerializedName("user")
        private User user;

        protected ReelItem(Parcel in) {
            date = in.readString();
            isLike = in.readByte() != 0;
            isProductShow = in.readByte() != 0;
            caption = in.readString();
            video = in.readString();
            screenshot = in.readString();
            mentionPeople = in.createStringArrayList();
            duration = in.readInt();
            createdAt = in.readString();
            isOriginalAudio = in.readByte() != 0;
            productImage = in.readString();
            analyticDate = in.readString();
            productTag = in.readString();
            V = in.readInt();
            hashtag = in.createStringArrayList();
            updatedAt = in.readString();
            thumbnail = in.readString();
            like = in.readInt();
            isDelete = in.readByte() != 0;
            allowComment = in.readByte() != 0;
            userId = in.readString();
            size = in.readString();
            showVideo = in.readInt();
            comment = in.readInt();
            location = in.readString();
            id = in.readString();
            productUrl = in.readString();
        }

        public String getDate() {
            return date;
        }

        public boolean isIsLike() {
            return isLike;
        }

        public void setIsLike(boolean like) {
            isLike = like;
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

        public String getProductImage() {
            return productImage;
        }

        public String getAnalyticDate() {
            return analyticDate;
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

        public Song getSong() {
            return song;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
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

        public String getProductUrl() {
            return productUrl;
        }

        public User getUser() {
            return user;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(date);
            dest.writeByte((byte) (isLike ? 1 : 0));
            dest.writeByte((byte) (isProductShow ? 1 : 0));
            dest.writeString(caption);
            dest.writeString(video);
            dest.writeString(screenshot);
            dest.writeStringList(mentionPeople);
            dest.writeInt(duration);
            dest.writeString(createdAt);
            dest.writeByte((byte) (isOriginalAudio ? 1 : 0));
            dest.writeString(productImage);
            dest.writeString(analyticDate);
            dest.writeString(productTag);
            dest.writeInt(V);
            dest.writeStringList(hashtag);
            dest.writeString(updatedAt);
            dest.writeString(thumbnail);
            dest.writeInt(like);
            dest.writeByte((byte) (isDelete ? 1 : 0));
            dest.writeByte((byte) (allowComment ? 1 : 0));
            dest.writeString(userId);
            dest.writeString(size);
            dest.writeInt(showVideo);
            dest.writeInt(comment);
            dest.writeString(location);
            dest.writeString(id);
            dest.writeString(productUrl);
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