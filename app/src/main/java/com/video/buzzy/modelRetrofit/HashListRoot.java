package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HashListRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("hashtag")
    private List<HashtagItem> hashtag;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public List<HashtagItem> getHashtag() {
        return hashtag;
    }

    public static class HashtagItem {

        @SerializedName("videoCount")
        private int videoCount;

        @SerializedName("_id")
        private String id;

        @SerializedName("hashtag")
        private String hashtag;

        public int getVideoCount() {
            return videoCount;
        }

        public String getId() {
            return id;
        }

        public String getHashtag() {
            return hashtag;
        }
    }
}