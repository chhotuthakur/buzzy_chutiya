package com.video.buzzy.modelRetrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchUserRoot {

    @SerializedName("search")
    private List<SearchItem> search;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<SearchItem> getSearch() {
        return search;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class SearchItem implements Parcelable {

        @SerializedName("gift")
        private List<Object> gift;

        @SerializedName("lastLogin")
        private String lastLogin;

        @SerializedName("isBlock")
        private boolean isBlock;

        @SerializedName("gender")
        private String gender;

        @SerializedName("loginType")
        private int loginType;

        @SerializedName("bio")
        private String bio;

        @SerializedName("isOnline")
        private boolean isOnline;

        @SerializedName("profileImage")
        private Object profileImage;

        @SerializedName("view")
        private int view;

        @SerializedName("analyticDate")
        private String analyticDate;

        @SerializedName("identity")
        private String identity;

        @SerializedName("coverImage")
        private Object coverImage;

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

        @SerializedName("isFollow")
        private boolean isFollow;

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

        public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
            @Override
            public SearchItem createFromParcel(Parcel in) {
                return new SearchItem(in);
            }

            @Override
            public SearchItem[] newArray(int size) {
                return new SearchItem[size];
            }
        };

        protected SearchItem(Parcel in) {
            lastLogin = in.readString();
            isBlock = in.readByte() != 0;
            loginType = in.readInt();
            bio = in.readString();
            isOnline = in.readByte() != 0;
            view = in.readInt();
            analyticDate = in.readString();
            identity = in.readString();
            fcmToken = in.readString();
            email = in.readString();
            like = in.readInt();
            requestForWithdrawDiamond = in.readInt();
            reels = in.readInt();
            isFollow = in.readByte() != 0;
            diamond = in.readInt();
            followers = in.readInt();
            following = in.readInt();
            name = in.readString();
            comment = in.readInt();
            id = in.readString();
            username = in.readString();
            coin = in.readInt();
        }

        public List<Object> getGift() {
            return gift;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public boolean isIsBlock() {
            return isBlock;
        }

        public Object getGender() {
            return gender;
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

        public Object getProfileImage() {
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

        public Object getCoverImage() {
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

        public boolean isIsFollow() {
            return isFollow;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(lastLogin);
            dest.writeByte((byte) (isBlock ? 1 : 0));
            dest.writeInt(loginType);
            dest.writeString(bio);
            dest.writeByte((byte) (isOnline ? 1 : 0));
            dest.writeInt(view);
            dest.writeString(analyticDate);
            dest.writeString(identity);
            dest.writeString(fcmToken);
            dest.writeString(email);
            dest.writeInt(like);
            dest.writeInt(requestForWithdrawDiamond);
            dest.writeInt(reels);
            dest.writeByte((byte) (isFollow ? 1 : 0));
            dest.writeInt(diamond);
            dest.writeInt(followers);
            dest.writeInt(following);
            dest.writeString(name);
            dest.writeInt(comment);
            dest.writeString(id);
            dest.writeString(username);
            dest.writeInt(coin);
        }
    }

    public static class Ad {

        @SerializedName("date")
        private Object date;

        @SerializedName("count")
        private int count;

        public Object getDate() {
            return date;
        }

        public int getCount() {
            return count;
        }
    }
}