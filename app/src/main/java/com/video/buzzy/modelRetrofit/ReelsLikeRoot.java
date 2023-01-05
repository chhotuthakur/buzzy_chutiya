package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

public class ReelsLikeRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;
    @SerializedName("isLike")
    private boolean isIsLike;

    public boolean isIsLike() {
        return isIsLike;
    }

    public void setIsLike(boolean isLike) {
        isIsLike = isLike;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}