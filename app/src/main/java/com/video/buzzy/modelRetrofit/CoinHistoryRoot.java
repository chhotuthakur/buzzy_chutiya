package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinHistoryRoot {

    @SerializedName("totalOutgoing")
    private int totalOutgoing;

    @SerializedName("totalIncome")
    private int totalIncome;

    @SerializedName("totalUser")
    private int totalUser;

    @SerializedName("history")
    private List<HistoryItem> history;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public int getTotalOutgoing() {
        return totalOutgoing;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public int getTotalUser() {
        return totalUser;
    }

    public List<HistoryItem> getHistory() {
        return history;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class HistoryItem {

        @SerializedName("date")
        private String date;

        @SerializedName("diamond")
        private int diamond;

        @SerializedName("analyticDate")
        private String analyticDate;

        @SerializedName("isIncome")
        private boolean isIncome;

        @SerializedName("planId")
        private Object planId;

        @SerializedName("_id")
        private String id;

        @SerializedName("type")
        private int type;

        @SerializedName("otherUserId")
        private Object otherUserId;

        @SerializedName("userId")
        private String userId;

        @SerializedName("coin")
        private int coin;

        @SerializedName("paymentGateway")
        private String paymentGateway;

        public String getDate() {
            return date;
        }

        public int getDiamond() {
            return diamond;
        }

        public String getAnalyticDate() {
            return analyticDate;
        }

        public boolean isIsIncome() {
            return isIncome;
        }

        public Object getPlanId() {
            return planId;
        }

        public String getId() {
            return id;
        }

        public int getType() {
            return type;
        }

        public Object getOtherUserId() {
            return otherUserId;
        }

        public String getUserId() {
            return userId;
        }

        public int getCoin() {
            return coin;
        }

        public String getPaymentGateway() {
            return paymentGateway;
        }
    }
}