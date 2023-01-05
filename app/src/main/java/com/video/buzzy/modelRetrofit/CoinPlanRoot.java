package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinPlanRoot {

    @SerializedName("coinPlan")
    private List<CoinPlanItem> coinPlan;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<CoinPlanItem> getCoinPlan() {
        return coinPlan;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class CoinPlanItem {

        @SerializedName("rupee")
        private int rupee;

        @SerializedName("createdAt")
        private String createdAt;

        public void setDelete(boolean delete) {
            isDelete = delete;
        }

        public int getRupee() {
            return rupee;
        }

        public void setRupee(int rupee) {
            this.rupee = rupee;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public String getId() {
            return id;
        }

        @SerializedName("isDelete")
        private boolean isDelete;

        @SerializedName("tag")
        private Object tag;

        @SerializedName("_id")
        private String id;

        @SerializedName("productKey")
        private String productKey;

        @SerializedName("coin")
        private int coin;

        @SerializedName("updatedAt")
        private String updatedAt;

        @SerializedName("dollar")
        private int dollar;

        public void setId(String id) {
            this.id = id;
        }

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getDollar() {
            return dollar;
        }

        public void setDollar(int dollar) {
            this.dollar = dollar;
        }
    }
}