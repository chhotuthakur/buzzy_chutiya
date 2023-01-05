package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedeemPlanRoot {

    @SerializedName("redeemPlan")
    private List<RedeemPlanItem> redeemPlan;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<RedeemPlanItem> getRedeemPlan() {
        return redeemPlan;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class RedeemPlanItem {

        @SerializedName("rupee")
        private int rupee;

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

        @SerializedName("dollar")
        private int dollar;

        @SerializedName("updatedAt")
        private String updatedAt;

        public int getRupee() {
            return rupee;
        }

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

        public int getDollar() {
            return dollar;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}