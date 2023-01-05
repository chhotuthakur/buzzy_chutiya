package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("setting")
    private Setting setting;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public Setting getSetting() {
        return setting;
    }

    public static class Setting {

        @SerializedName("isAppActive")
        private boolean isAppActive;

        @SerializedName("maxSecondForVideo")
        private int maxSecondForVideo;

        @SerializedName("googlePlayKey")
        private String googlePlayKey;

        @SerializedName("googlePlayEmail")
        private String googlePlayEmail;

        @SerializedName("stripeSecretKey")
        private String stripeSecretKey;

        @SerializedName("diamondForCashOut")
        private int diamondForCashOut;

        @SerializedName("stripePublishableKey")
        private String stripePublishableKey;

        @SerializedName("stripeSwitch")
        private boolean stripeSwitch;

        @SerializedName("maxAdPerDay")
        private int maxAdPerDay;

        @SerializedName("privacyPolicyLink")
        private String privacyPolicyLink;

        @SerializedName("freeCoinForAd")
        private int freeCoinForAd;

        @SerializedName("minDiamondForCashOut")
        private int minDiamondForCashOut;

        @SerializedName("googlePlaySwitch")
        private boolean googlePlaySwitch;

        @SerializedName("privacyPolicyText")
        private String privacyPolicyText;

        @SerializedName("currency")
        private String currency;

        @SerializedName("_id")
        private String id;

        @SerializedName("diamondPerCurrency")
        private int diamondPerCurrency;

        @SerializedName("CoinForDiamond")
        private int coinForDiamond;

        @SerializedName("paymentGateway")
        private List<String> paymentGateway;

        public boolean isIsAppActive() {
            return isAppActive;
        }

        public int getMaxSecondForVideo() {
            return maxSecondForVideo;
        }

        public String getGooglePlayKey() {
            return googlePlayKey;
        }

        public String getGooglePlayEmail() {
            return googlePlayEmail;
        }

        public String getStripeSecretKey() {
            return stripeSecretKey;
        }

        public int getDiamondForCashOut() {
            return diamondForCashOut;
        }

        public String getStripePublishableKey() {
            return stripePublishableKey;
        }

        public boolean isStripeSwitch() {
            return stripeSwitch;
        }

        public int getMaxAdPerDay() {
            return maxAdPerDay;
        }

        public String getPrivacyPolicyLink() {
            return privacyPolicyLink;
        }

        public int getFreeCoinForAd() {
            return freeCoinForAd;
        }

        public int getMinDiamondForCashOut() {
            return minDiamondForCashOut;
        }

        public boolean isGooglePlaySwitch() {
            return googlePlaySwitch;
        }

        public String getPrivacyPolicyText() {
            return privacyPolicyText;
        }

        public String getCurrency() {
            return currency;
        }

        public String getId() {
            return id;
        }

        public int getDiamondPerCurrency() {
            return diamondPerCurrency;
        }

        public int getCoinForDiamond() {
            return coinForDiamond;
        }

        public List<String> getPaymentGateway() {
            return paymentGateway;
        }
    }
}