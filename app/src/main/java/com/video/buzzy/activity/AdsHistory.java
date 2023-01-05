package com.video.buzzy.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivityAdsHistoryBinding;
import com.video.buzzy.modelRetrofit.CoinHistoryRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.HistoryViewModel;
import com.video.buzzy.viewmodel.ViewModelFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsHistory extends AppCompatActivity {

    ActivityAdsHistoryBinding binding;
    HistoryViewModel viewModel;
    SessionManager sessionManager;
    private boolean isLoding = false;
    private int start = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ads_history);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new HistoryViewModel()).createFor()).get(HistoryViewModel.class);

        initView();
    }

    private void initView() {

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        binding.rvAdsHistory.setAdapter(viewModel.adsCoinHistory);

        sessionManager = new SessionManager(this);

        binding.rvAdsHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollHorizontally(1)) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemcount = manager.getChildCount();
                    int totalitem = manager.getItemCount();
                    int firstvisibleitempos = manager.findFirstCompletelyVisibleItemPosition();
                    if (!isLoding && (visibleItemcount + firstvisibleitempos >= totalitem) && firstvisibleitempos > 0) {
                        isLoding = true;
                        start = start + Const.LIMIT;
                        CoinHistory("ad");
                    }
                }
            }
        });

        CoinHistory("ad");

        binding.back.setOnClickListener(v -> {
            finish();
        });

    }


    public void CoinHistory(String type) {
        Call<CoinHistoryRoot> call = RetrofitBuilder.create().getCoinHistory(sessionManager.getUser().getId(), type, start, Const.LIMIT, "ALL", "ALL");

        call.enqueue(new Callback<CoinHistoryRoot>() {
            @Override
            public void onResponse(Call<CoinHistoryRoot> call, Response<CoinHistoryRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getHistory().isEmpty()) {
                        binding.rvAdsHistory.setVisibility(View.VISIBLE);
                        binding.nodataLay.setVisibility(View.GONE);
                        viewModel.adsCoinHistory.clear();
                        viewModel.adsCoinHistory.addData(response.body().getHistory());
                    } else if (start == 1) {
                        binding.rvAdsHistory.setVisibility(View.GONE);
                        binding.nodataLay.setVisibility(View.VISIBLE);
                    }

                    isLoding = false;
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
            }

            @Override
            public void onFailure(Call<CoinHistoryRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.rvAdsHistory.setVisibility(View.GONE);
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }
        });
    }
}