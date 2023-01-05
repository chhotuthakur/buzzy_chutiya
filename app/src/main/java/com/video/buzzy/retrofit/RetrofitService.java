package com.video.buzzy.retrofit;

import com.google.gson.JsonObject;
import com.video.buzzy.modelRetrofit.AdsRoot;
import com.video.buzzy.modelRetrofit.AllVideoRoot;
import com.video.buzzy.modelRetrofit.BannerRoot;
import com.video.buzzy.modelRetrofit.ChatRoot;
import com.video.buzzy.modelRetrofit.ChatThumbList;
import com.video.buzzy.modelRetrofit.ChatTopic;
import com.video.buzzy.modelRetrofit.CoinHistoryRoot;
import com.video.buzzy.modelRetrofit.CoinPlanRoot;
import com.video.buzzy.modelRetrofit.CustomAdsRoot;
import com.video.buzzy.modelRetrofit.DiamondPlanRoot;
import com.video.buzzy.modelRetrofit.FacebookLoginRoot;
import com.video.buzzy.modelRetrofit.GiftRoot;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;
import com.video.buzzy.modelRetrofit.HistoryRoot;
import com.video.buzzy.modelRetrofit.LocationRoot;
import com.video.buzzy.modelRetrofit.NotificationRoot;
import com.video.buzzy.modelRetrofit.RedeeHistoryRoot;
import com.video.buzzy.modelRetrofit.RedeemDiamondRoot;
import com.video.buzzy.modelRetrofit.RedeemPlanRoot;
import com.video.buzzy.modelRetrofit.ReelCommentRoot;
import com.video.buzzy.modelRetrofit.ReelsLikeRoot;
import com.video.buzzy.modelRetrofit.ReelsVideoRoot;
import com.video.buzzy.modelRetrofit.RestResponse;
import com.video.buzzy.modelRetrofit.SearchUserRoot;
import com.video.buzzy.modelRetrofit.SettingRoot;
import com.video.buzzy.modelRetrofit.SongRoot;
import com.video.buzzy.modelRetrofit.StickerRoot;
import com.video.buzzy.modelRetrofit.StripePaymentRoot;
import com.video.buzzy.modelRetrofit.TrendingUserRoot;
import com.video.buzzy.modelRetrofit.UploadImageRoot;
import com.video.buzzy.modelRetrofit.UserFollowersRoot;
import com.video.buzzy.modelRetrofit.UserRoot;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    @POST("/user")
    Call<UserRoot> createUser(@Body JsonObject jsonObject);

    @PATCH("/user")
    Call<UserRoot> notiOnOff(@Query("userId") String userid);

    @GET("/user/profile")
    Call<UserRoot> getProfile(@Query("userId") String userid);

    @Multipart
    @PATCH("/user/update")
    Call<UserRoot> updateUser(@PartMap Map<String, RequestBody> partMap,
                              @Part MultipartBody.Part requestBody);

    @GET("/gift/all")
    Call<GiftRoot> getAllGift(@Query("gift") String giftType);


    @GET("/banner/all")
    Call<BannerRoot> getAllBanner();

    @GET("/user/trending")
    Call<TrendingUserRoot> getTrendingUser(@Query("userId") String userid, @Query("start") int start, @Query("limit") int limit);

    @GET("/hashtag/hashtag")
    Call<HashtagVideoRoot> getHashtagVideo(@Query("type") String type, @Query("start") int start, @Query("limit") int limit);

    @GET("/song")
    Call<SongRoot> getSong();

    @GET("/user/otherProfile")
    Call<UserRoot> getOtherUser(@Query("fromUserId") String fromuserid, @Query("toUserId") String touserid);

    @GET("/reels")
    Call<GiftRoot> getUserGift();

    @POST("/follower")
    Call<RestResponse> setFollowOrUnfollow(@Body JsonObject jsonObject);

    @Multipart
    @POST("/reels/upload")
    Call<ReelsVideoRoot> uploadReels(@PartMap Map<String, RequestBody> partMap,
                                     @Part MultipartBody.Part requestBody1,
                                     @Part MultipartBody.Part requestBody2,
                                     @Part MultipartBody.Part requestBody3,
                                     @Part MultipartBody.Part requestBody4);


    @POST("/user/userSearch")
    Call<SearchUserRoot> searchUser(@Body JsonObject jsonObject);

    @GET("/location/search")
    Call<LocationRoot> getLocationList(@Query("value") String userId);

    @GET("/follower/followers")
    Call<UserFollowersRoot> getFollowersList(@Query("userId") String userId, @Query("search") String search, @Query("start") int start, @Query("limit") int limit);

    @GET("/follower/following")
    Call<UserFollowersRoot> getFollowingList(@Query("userId") String userId, @Query("search") String search, @Query("start") int start, @Query("limit") int limit);

    @GET("/reels/getReel")
    Call<AllVideoRoot> getAllReelsVideo(@Query("userId") String userId, @Query("start") int start, @Query("limit") int limit);

    @POST("/like")
    Call<ReelsLikeRoot> getReelsLikedOrNot(@Body JsonObject jsonObject);

    @GET("/notification")
    Call<NotificationRoot> getTypeOfNotification(@Query("type") String type, @Query("userId") String userId);

    @GET("/user/username")
    Call<UserRoot> getProfileByUsername(@Query("userId") String userId, @Query("username") String username);

    @GET("/comment")
    Call<ReelCommentRoot> getReelsComment(@Query("reelId") String reelId);

    @POST("/comment")
    Call<ReelCommentRoot> postComment(@Body JsonObject jsonObject);

    @POST("/gift/sendGift")
    Call<UserRoot> sendGift(@Body JsonObject jsonObject);

    @GET("/chatTopic")
    Call<ChatThumbList> getChatThumbList(@Query("userId") String userId);

    @POST("/chatTopic")
    Call<ChatTopic> createChatTopic(@Body JsonObject jsonObject);

    @Multipart
    @POST("/chat")
    Call<UploadImageRoot> sendChatViaImage(@PartMap Map<String, RequestBody> partMap,
                                           @Part MultipartBody.Part requestBody1);

    @GET("/chat")
    Call<ChatRoot> getOldChat(@Query("topicId") String topicId, @Query("start") int start, @Query("limit") int limit);

    @PUT("/user")
    Call<UserRoot> makeUserOnline(@Query("userId") String userId);

    @DELETE("/notification")
    Call<RestResponse> deleteNotification(@Query("notificationId") String notificationId);

    @GET("/coinPlan")
    Call<CoinPlanRoot> getPlanRoot();

    @GET("/setting")
    Call<SettingRoot> getSetting();

    @GET("/ad")
    Call<AdsRoot> getAds();

    @POST("/wallet/income/seeAd")
    Call<UserRoot> getCoinViewingAds(@Query("userId") String userId);

    @POST("/coinPlan/purchase/googlePlay")
    Call<UserRoot> callGooglePay(@Body JsonObject jsonObject);

    @POST("/coinPlan/purchase/stripe")
    Call<StripePaymentRoot> callStripePay(@Body JsonObject jsonObject);

    @POST("/coinPlan/purchase/stripe")
    Call<UserRoot> purchsePlanStripeDiamons(@Body JsonObject jsonObject);

    @GET("/diamondPlan")
    Call<DiamondPlanRoot> getDiamondPlan();

    @GET("/redeemPlan")
    Call<RedeemPlanRoot> getRedeemPlan();

    @POST("diamondPlan/addPlan")
    Call<UserRoot> ConvertDiamondToCoin(@Body JsonObject jsonObject);

    @POST("/redeem")
    Call<RedeemDiamondRoot> RedeemDiamond(@Body JsonObject jsonObject);

    @GET("/redeemPlan")
    Call<HistoryRoot> getHistory();

    @GET("/sticker")
    Call<StickerRoot> getSticker();

    @GET("/customAd")
    Call<CustomAdsRoot> getCustomAds();

    @GET("/reels/userWiseReelAndroid")
    Call<AllVideoRoot> getUserWiseReels(@Query("userId") String userId);

    @GET("/redeem/history")
    Call<RedeeHistoryRoot> getRedeemHistory(@Query("userId") String userId, @Query("type") String type, @Query("start") int start, @Query("limit") int limit);

    @GET("/wallet/history")
    Call<CoinHistoryRoot> getCoinHistory(@Query("userId") String userId, @Query("type") String type, @Query("start") int start, @Query("limit") int limit, @Query("startDate") String startDate, @Query("endDate") String endDate);

    @GET("/v3.2/{url}")
    Call<FacebookLoginRoot> singupfb(@Path("url") String id, @Query("fields") String fields, @Query("access_token") String token);


}

