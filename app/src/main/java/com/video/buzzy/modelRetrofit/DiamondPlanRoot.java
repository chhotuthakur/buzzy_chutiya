package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiamondPlanRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("diamondPlan")
    private List<DiamondPlanItem> diamondPlan;

    @SerializedName("status")
    private boolean status;

    public String getMessage() {
        return message;
    }

    public List<DiamondPlanItem> getDiamondPlan() {
        return diamondPlan;
    }

    public boolean isStatus() {
        return status;
    }

    public static class DiamondPlanItem {

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("diamond")
        private int diamond;

        @SerializedName("isDelete")
        private boolean isDelete;

        @SerializedName("_id")
        private String id;

        @SerializedName("tag")
        private Object tag;

        @SerializedName("coin")
        private int coin;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getCreatedAt() {
            return createdAt;
        }

        public int getDiamond() {
            return diamond;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public String getId() {
            return id;
        }

        public Object getTag() {
            return tag;
        }

        public int getCoin() {
            return coin;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}