package com.video.buzzy.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.video.buzzy.adapter.GiftAdapter;
import com.video.buzzy.adapter.GiftNotificationAdapter;
import com.video.buzzy.adapter.GiftProfileAdapter;
import com.video.buzzy.modelRetrofit.GiftRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GiftViewModel extends ViewModel {

    public MutableLiveData<Integer> userTotalGift = new MutableLiveData<>();
    public MutableLiveData<GiftRoot.GiftItem> finalgift = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSend = new MutableLiveData<>(false);
    public GiftAdapter giftAdapter = new GiftAdapter();
    public GiftProfileAdapter giftProfileAdapter = new GiftProfileAdapter();
    public GiftNotificationAdapter giftNotificationAdapter = new GiftNotificationAdapter();
    public MutableLiveData<GiftRoot.GiftItem> Selectedgift = new MutableLiveData<>();


    @Override
    protected void onCleared() {
        isSend.postValue(false);
        giftAdapter.clear();
        super.onCleared();
    }


    public void clearALl() {
        onCleared();
    }

    public void getGift() {
        Call<GiftRoot> call = RetrofitBuilder.create().getAllGift("all");

        call.enqueue(new Callback<GiftRoot>() {
            @Override
            public void onResponse(Call<GiftRoot> call, Response<GiftRoot> response) {
                Log.d("TAG", "onResponse: ");
                if (response.code() == 200 && response.isSuccessful() && !response.body().getGift().isEmpty()) {
                    Log.d("TAG", "onResponse: " + response.body().getGift().toString());

                    giftAdapter.clear();
                    giftAdapter.addData(response.body().getGift());

                }
            }

            @Override
            public void onFailure(Call<GiftRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }
}
