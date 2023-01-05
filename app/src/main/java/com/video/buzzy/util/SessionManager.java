package com.video.buzzy.util;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.BuildConfig;
import com.video.buzzy.activity.UploadActivity;
import com.video.buzzy.modelRetrofit.AdsRoot;
import com.video.buzzy.modelRetrofit.CustomAdsRoot;
import com.video.buzzy.modelRetrofit.SettingRoot;
import com.video.buzzy.modelRetrofit.UserRoot;

import java.util.ArrayList;
import java.util.List;


public class SessionManager {
    private static final String USER_STR = "local_user";
    private static final String SETTING = "setting";
    private static final String SEARCHHISTORY = "searchhistry";
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private String ADVERTISEMENT = "ads";
    private String CUSTOMADVERTISEMENT = "customads";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.pref = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        this.editor = this.pref.edit();
    }

//    public static String getUserId(Context context) {
//        SessionManager sessionManager = new SessionManager(context);
//        if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
//            return sessionManager.getUser().getId();
//        }
//        return "";
//    }


    public void saveBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        return pref.getBoolean(key, false);
    }

    public void saveStringValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return pref.getString(key, "");
    }


    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }


    public void saveUser(UserRoot.User user) {
        editor.putString(USER_STR, new Gson().toJson(user));
        editor.apply();


    }

    public UserRoot.User getUser() {
        String userString = pref.getString(USER_STR, "");
        if (userString != null && !userString.isEmpty()) {
            return new Gson().fromJson(userString, UserRoot.User.class);
        }
        return null;
    }


    // workmanager ma big data transfer nathi thato so local ma save kari ne tya get karvama aave 6
    public void saveLocalVideo(UploadActivity.LocalVideo userDummy) {
        editor.putString("localvid", new Gson().toJson(userDummy));
        editor.apply();


    }

    public UploadActivity.LocalVideo getLocalVideo() {
        String userString = pref.getString("localvid", "");
        if (userString != null && !userString.isEmpty()) {
            return new Gson().fromJson(userString, UploadActivity.LocalVideo.class);
        }
        return null;
    }

    public void saveSetting(SettingRoot.Setting setting) {
        editor.putString(SETTING, new Gson().toJson(setting));
        editor.apply();
    }

    public SettingRoot.Setting getSetting() {
        String userString = pref.getString(SETTING, "");
        if (userString != null && !userString.isEmpty()) {
            return new Gson().fromJson(userString, SettingRoot.Setting.class);
        }
        return null;
    }

    public void saveAds(AdsRoot.Advertisement ads) {
        editor.putString(ADVERTISEMENT, new Gson().toJson(ads));
        editor.apply();
    }

    public AdsRoot.Advertisement getAds() {
        String userString = pref.getString(ADVERTISEMENT, "");
        if (userString != null && !userString.isEmpty()) {
            return new Gson().fromJson(userString, AdsRoot.Advertisement.class);
        }
        return null;
    }

    public void saveOwnAds(List<CustomAdsRoot.CustomAdItem> ownAdItem) {
        editor.putString(Const.OWNADS, new Gson().toJson(ownAdItem));
        editor.apply();
    }

    public List<CustomAdsRoot.CustomAdItem> getOwnads() {
        String userString = pref.getString(Const.OWNADS, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, new TypeToken<ArrayList<CustomAdsRoot.CustomAdItem>>() {
            }.getType());
        }
        return new ArrayList<>();
    }


}
