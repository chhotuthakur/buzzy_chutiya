package com.video.buzzy.util;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Const {

    public static final int REQUEST_CODE_PERMISSIONS_UPLOAD = 200;
    public static final long MAX_DURATION = TimeUnit.SECONDS.toMillis(30);
    public static final long MIN_DURATION = TimeUnit.SECONDS.toMillis(5);
    public static final int MAX_RESOLUTION = 960;
    public static final int REQUEST_CODE_PICK_VIDEO = 100;
    public static final int REQUEST_CODE_PICK_SONG_FILE = 60600 + 6;
    public static final int NOTIFICATION_DOWNLOAD = 60600 + 1;
    public static final int REQUEST_CODE_PICK_SONG = 60600 + 5;
    public static final int REQUEST_CODE_PICK_STICKER = 60600 + 7;
    public static final String TEMP_FOLDER_NAME = "TempFolder";
    public static final int NOTIFICATION_GENERATE_PREVIEW = 60600 + 2;
    public static final int NOTIFICATION_UPLOAD = 60600 + 4;
    public static final int NOTIFICATION_UPLOAD_FAILED = 60600 + 5;
    public static final int MAX_CHAR_HSHTAG = 10;
    public static final String USERLIST = "userlist";
    public static final String CHATUSERNAME = "chatusername";
    public static final String REELSUSERIMAGE = "reelsuserimage";
    public static final String CHATUSERIMAGE = "chatuserimage";
    public static final String KEY = "key";
    public static final String TRENDUSER = "trenduser";
    public static final String ISFROMREEL = "isfromreel";
    public static final String SEARCHHASHTAGTEXT = "searchhashtagstring";
    public static final String FINDUSERPROFILEBYNAME = "findprofilebyusername";
    public static final String REELSID = "reelsid";
    public static final String REEL = "reel";
    public static final String REELSTRING = "reelstring";
    public static final String USERIDFORGETPROFILE = "userid";
    public static final String VIDEOUSERID = "videouserid";
    public static final String EVENT_CHAT = "chat";
    public static final String EVENT_GIFT = "gift";
    public static final int LIMIT = 10;
    public static final String TYPE = "type";
    public static final String NOTIFICATION_TOKEN = "fcmtoken";
    public static final String POSITION = "position";
    public static final String CHATROOM = "chatroom";
    public static final String USERID = "userid";
    public static final String OTHERUSERFOLLOWER = "otheruserfollower";
    public static final String FOLLOWERUSERNAME = "followerusername";
    public static final String SEARVICEINTENT = "serviceintent";
    public static final String SERVICEREEL = "servicereel";
    public static final String ISFROMCHAT = "isfromchat";
    public static final String IMAGEPATH = "imagepath";
    public static final String REELPOS = "reelpos";
    public static final String REELSDATA = "reelsdata";
    public static final String USERREELPOS = "userreelpos";
    public static final String REELUSER = "reeluser";
    public static String LOGINUSERID;
    public static String HASHTAG = "hashtag";
    public static String USERNAMELIST = "usernamelist";
    public static String USERIMAGE = "userimage";
    public static String DATA = "data";
    public static String REELSUSERNAME = "reelsusername";
    public static String TestPurchase = "android.test.purchased";
    public static String ISLOGIN = "login";
    public static String Currency = "currency";
    public static String Dummy_image = "storage/female.png";
    public static String URL = "url";
    public static String CHATUSERId = "chatuserid";
    public static String AdsDownload = "adsdownload";
    public static String OWNADS = "ownads";

    public static ArrayList<String> listFilterThumb(Context context) {
        ArrayList<String> typefaceArrayList = new ArrayList<>();
        String[] list;
        try {
            list = context.getAssets().list("filter");
            assert list != null;
            if (list.length > 0) {
                for (String file : list) {
                    typefaceArrayList.add("file:///android_asset/filter/" + file);
                }
            }
        } catch (IOException e) {
            return null;
        }
        return typefaceArrayList;
    }

    public static ArrayList<String> listAssetFilterThumb(Context context) {
        ArrayList<String> typefaceArrayList = new ArrayList<>();
        String[] list;
        try {
            list = context.getAssets().list("filterthumb");
            assert list != null;
            if (list.length > 0) {
                for (String file : list) {
                    typefaceArrayList.add("file:///android_asset/filterthumb/" + file);
                }
            }
        } catch (IOException e) {
            return null;
        }
        return typefaceArrayList;
    }



}
