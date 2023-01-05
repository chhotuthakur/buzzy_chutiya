package com.video.buzzy.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.video.buzzy.adapter.HashtagReelsAdapter;
import com.video.buzzy.ads.MultipleCustomNativeAds;
import com.video.buzzy.modelRetrofit.CustomAdsRoot;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReelsOfHashtagViewModel extends ViewModel {

    public HashtagReelsAdapter hashtagReelsAdapter = new HashtagReelsAdapter();

    public int start = 0;
    public ObservableBoolean isFirstTimeLoading = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreLoading = new ObservableBoolean(true);
    public ObservableBoolean noData = new ObservableBoolean(false);
    public MutableLiveData<Boolean> isLoadCompleted = new MutableLiveData<>();
    SessionManager sessionManager;
    Context context;


    public void getReliteData(boolean isLoadMore, String hashtag, int pos) {
        if (isLoadMore) {
            Log.d("hashrelite", "getReliteData:  load more");
            start = start + Const.LIMIT;
            isLoadMoreLoading.set(true);
        } else {
            Log.d("hashrelite", "getReliteData:  start 0");
            start = 0;
            hashtagReelsAdapter.clear();
            isFirstTimeLoading.set(true);
        }

        noData.set(false);
        Log.d("hashrelite", "getReliteData:  out of else");

        Call<HashtagVideoRoot> call = RetrofitBuilder.create().getHashtagVideo(hashtag, start, Const.LIMIT);

        Log.d("hashrelite", "getReliteData:  call " + RetrofitBuilder.create().getHashtagVideo(hashtag, start, Const.LIMIT).toString());

        call.enqueue(new Callback<HashtagVideoRoot>() {
            @Override
            public void onResponse(Call<HashtagVideoRoot> call, Response<HashtagVideoRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getHashtag().isEmpty()) {
                        for (int i = 0; i < response.body().getHashtag().size(); i++) {
                            Log.d("hashrelite", "onResponse: " + response.body().getHashtag().size());
                            hashtagReelsAdapter.clear();
                            HashtagVideoRoot.HashtagItem hashtag = response.body().getHashtag().get(i);
                            hashtagReelsAdapter.addData(hashtag.getReel());
                            loadNativeAds();
                            //hashtagReelsAdapter.playVideoAt(pos);
                        }

                        if (start == 0) {
                            Log.d("hashrelite", "onResponse: ");
                            hashtagReelsAdapter.playVideoAt(0);
                        }

                    } else if (start == 0) {
                        Log.d("hashrelite", "onResponse: reels empty");
                        noData.set(true);
                    }
                }
                isLoadCompleted.postValue(true);
            }

            @Override
            public void onFailure(Call<HashtagVideoRoot> call, Throwable t) {
                Log.d("hashrelite", "onFailure: " + t.getMessage());
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    public void loadNativeAds() {
        Log.d("TAG", "loadNativeAds: ");
        if (context == null) return;
        SessionManager sessionManager = new SessionManager(context);

        Collections.shuffle(sessionManager.getOwnads());
        CustomAdsRoot.CustomAdItem customAdItem = sessionManager.getOwnads().get(0);

        if (sessionManager.getAds().isShow()) {
            new MultipleCustomNativeAds(context, (adsData, position) -> {
                if (hashtagReelsAdapter != null) {
                    if (adsData instanceof com.google.android.gms.ads.nativead.NativeAd) {
                        Log.d("TAG", "loadNativeAds: google mainact");
                        hashtagReelsAdapter.addNewAds(position, (com.google.android.gms.ads.nativead.NativeAd) adsData);
                    }
                    return position < hashtagReelsAdapter.getList().size();
                }
                return true;
            }, 4);

        } else if (customAdItem.isShow()) {
            if (hashtagReelsAdapter != null) {
                for (int i = 0; i < hashtagReelsAdapter.getList().size(); i++) {
                    if (i % 3 == 0) {
                        if (i != 0) {
                            Log.d("TAG", "loadNativeAds: dfdfd");
                            hashtagReelsAdapter.addCustom(i);
                        }
                    }
                }
            }
        }
    }


}
