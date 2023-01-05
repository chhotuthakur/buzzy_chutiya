package com.video.buzzy.viewmodel;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.video.buzzy.adapter.UsersReelsAdapter;
import com.video.buzzy.modelRetrofit.AllVideoRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReelsOfUserViewModel extends ViewModel {

    public UsersReelsAdapter usersReelsAdapter = new UsersReelsAdapter();
    public int start = 0;
    public ObservableBoolean isFirstTimeLoading = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreLoading = new ObservableBoolean(true);
    public ObservableBoolean noData = new ObservableBoolean(false);
    public MutableLiveData<Boolean> isLoadCompleted = new MutableLiveData<>();



    public void getReliteData(boolean isLoadMore, String loginUserId, String OtheruserId) {
        if (isLoadMore) {
            start = start + Const.LIMIT;
            isLoadMoreLoading.set(true);
        } else {
            start = 0;
            usersReelsAdapter.clear();
            isFirstTimeLoading.set(true);
        }

        noData.set(false);

        Call<AllVideoRoot> call = RetrofitBuilder.create().getUserWiseReels(OtheruserId);
        call.enqueue(new Callback<AllVideoRoot>() {
            @Override
            public void onResponse(Call<AllVideoRoot> call, Response<AllVideoRoot> response) {
                if (response.code() == 200) {

                    if (response.code() == 200 && response.isSuccessful() && !response.body().getReel().isEmpty()) {
                        Log.d("TAG", "onResponse: ");
                        usersReelsAdapter.addData(response.body().getReel());

                        if (start == 0) {
                            usersReelsAdapter.playVideoAt(0);
                        }

                    } else if (start == 0) {
                        noData.set(true);
                    }
                }
                isLoadCompleted.postValue(true);
            }

            @Override
            public void onFailure(Call<AllVideoRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());

            }
        });
    }


}
