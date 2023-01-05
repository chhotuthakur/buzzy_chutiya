package com.video.buzzy.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.video.buzzy.adapter.ReelsAdapter;
import com.video.buzzy.ads.MultipleCustomNativeAds;
import com.video.buzzy.databinding.FragmentHomeBinding;
import com.video.buzzy.modelRetrofit.AllVideoRoot;
import com.video.buzzy.modelRetrofit.CustomAdsRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReelsViewModel extends ViewModel {

    public ReelsAdapter reelsAdapter = new ReelsAdapter();

    public int start = 0;
    public ObservableBoolean isFirstTimeLoading = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreLoading = new ObservableBoolean(true);
    public ObservableBoolean noData = new ObservableBoolean(false);
    public MutableLiveData<Boolean> isLoadCompleted = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFromBranch = new MutableLiveData<>(false);
    List<AllVideoRoot.ReelItem> reels = new ArrayList<>();
    Context context;
    FragmentHomeBinding homebinding;


    public void initModel(Context context) {
        this.context = context;
    }

    public void getReliteData(boolean isLoadMore, String userId, FragmentHomeBinding binding, String branchPath) {

        homebinding = binding;

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        if (isLoadMore) {
            start = start + Const.LIMIT;
            isLoadMoreLoading.set(true);
        } else {
            start = 0;
            reelsAdapter.clear();
            isFirstTimeLoading.set(true);
        }

        noData.set(false);

        Call<AllVideoRoot> call = RetrofitBuilder.create().getAllReelsVideo(userId, start, Const.LIMIT);
        call.enqueue(new Callback<AllVideoRoot>() {
            @Override
            public void onResponse(Call<AllVideoRoot> call, Response<AllVideoRoot> response) {
                if (response.code() == 200 && response.body().isStatus()) {
                    if (!response.body().getReel().isEmpty()) {

                        if (branchPath != null && !branchPath.equals("")) {
                            reels = new Gson().fromJson(branchPath, new TypeToken<ArrayList<AllVideoRoot.ReelItem>>() {
                            }.getType());

                            binding.nodataLay.setVisibility(View.GONE);
                            reelsAdapter.addData(response.body().getReel());
                            loadNativeAds();

                            if (start == 0) {
                                reelsAdapter.playVideoAt(0);
                            }
                        } else {
                            binding.nodataLay.setVisibility(View.GONE);
                            reelsAdapter.addData(response.body().getReel());
                            loadNativeAds();
                            if (start == 0) {
                                reelsAdapter.playVideoAt(0);
                            }
                        }
                    } else if (start == 0) {
                        //noData.set(true);
                        binding.buffering.setVisibility(View.GONE);
                        binding.nodataLay.setVisibility(View.VISIBLE);
                    }
                }
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                isLoadCompleted.postValue(true);
            }

            @Override
            public void onFailure(Call<AllVideoRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
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
                if (reelsAdapter != null) {
                    if (adsData instanceof com.google.android.gms.ads.nativead.NativeAd) {
                        Log.d("TAG", "loadNativeAds: google mainact");
                        reelsAdapter.addNewAds(position, (com.google.android.gms.ads.nativead.NativeAd) adsData);
                        homebinding.aboveLay.setVisibility(View.GONE);
                    }
                    return position < reelsAdapter.getList().size();
                }
                return true;
            }, 4);

        } else if (customAdItem.isShow()) {
            if (reelsAdapter != null) {
                for (int i = 0; i < reelsAdapter.getList().size(); i++) {
                    if (i % 3 == 0) {
                        if (i != 0) {
                            Log.d("TAG", "loadNativeAds: dfdfd");
                            reelsAdapter.addCustom(i);
                            homebinding.aboveLay.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }


}
