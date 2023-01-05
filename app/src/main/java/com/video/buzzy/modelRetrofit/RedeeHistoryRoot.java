package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedeeHistoryRoot {

    @SerializedName("redeem")
    private List<RedeemItem> redeem;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<RedeemItem> getRedeem() {
        return redeem;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class RedeemItem {

        @SerializedName("rupee")
        private String rupee;

        @SerializedName("date")
        private String date;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("diamond")
        private int diamond;

        @SerializedName("description")
        private String description;

        @SerializedName("planId")
        private String planId;

        @SerializedName("_id")
        private String id;

        @SerializedName("userId")
        private String userId;

        @SerializedName("dollar")
        private String dollar;

        @SerializedName("status")
        private int status;

        @SerializedName("paymentGateway")
        private String paymentGateway;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getRupee() {
            return rupee;
        }

        public String getDate() {
            return date;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getDiamond() {
            return diamond;
        }

        public String getDescription() {
            return description;
        }

        public String getPlanId() {
            return planId;
        }

        public String getId() {
            return id;
        }

        public String getUserId() {
            return userId;
        }

        public String getDollar() {
            return dollar;
        }

        public int getStatus() {
            return status;
        }

        public String getPaymentGateway() {
            return paymentGateway;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}