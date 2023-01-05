package com.video.buzzy.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.Player;
import com.google.gson.Gson;
import com.video.buzzy.R;
import com.video.buzzy.adapter.BannerAdapter;
import com.video.buzzy.adapter.DotAdapter;
import com.video.buzzy.adapter.HashtagAllAdapter;
import com.video.buzzy.databinding.FragmentSearchBinding;
import com.video.buzzy.modelRetrofit.BannerRoot;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements Player.EventListener {
    FragmentSearchBinding binding;
    HashtagAllAdapter hashtagAllAdapter;
    BannerAdapter bannerAdapter;
    NavController navController;
    DotAdapter dotAdapter;
    private int pos = 0;
    private int start = 0;
    private boolean isLoding = false;

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        navController = Navigation.findNavController(requireActivity(), R.id.host);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        bannerAdapter = new BannerAdapter();
        binding.rvBanner.setAdapter(bannerAdapter);

        hashtagAllAdapter = new HashtagAllAdapter();
        binding.rvAllHashtag.setAdapter(hashtagAllAdapter);

        getAllBanner();
        settop();
        getHashtagVideo();

        hashtagAllAdapter.setOnViewAllClick(new HashtagAllAdapter.OnViewAllClick() {
            @Override
            public void onClick(String hashtag) {
                Bundle b = new Bundle();
                b.putString(Const.SEARCHHASHTAGTEXT, hashtag);
                navController.navigate(R.id.action_searchFragment_to_viewHashtag, b);
            }

            @Override
            public void onVideoClick(List<HashtagVideoRoot.ReelItem> reelItemList, int pos) {
                Bundle b = new Bundle();
                b.putString(Const.REELSDATA, new Gson().toJson(reelItemList));
                b.putInt(Const.REELPOS, pos);
                navController.navigate(R.id.action_searchFragment_to_reelsFragment, b);
            }


        });

        binding.seachLay.setOnClickListener(v -> {
            navController.navigate(R.id.action_searchFragmemt_to_searchUserHashFragment);
        });

        binding.rvBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    dotAdapter.changePos(pos);

                }
            }
        });

        binding.rvAllHashtag.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        getHashtagVideo();
                    }
                }
            }
        });


    }

    public void settop() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int pos = 0;
            boolean flag = true;

            @Override
            public void run() {
                if (pos == bannerAdapter.getItemCount() - 1) {
                    flag = false;
                } else if (pos == 0) {
                    flag = true;
                }
                if (flag) {
                    pos++;
                } else {
                    pos--;
                }
                binding.rvBanner.smoothScrollToPosition(pos);
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);

    }

    public void getAllBanner() {
        Call<BannerRoot> call = RetrofitBuilder.create().getAllBanner();
        call.enqueue(new Callback<BannerRoot>() {
            @Override
            public void onResponse(Call<BannerRoot> call, Response<BannerRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && !response.body().getBanner().isEmpty()) {
                    bannerAdapter.addData(response.body().getBanner());

                    new PagerSnapHelper().attachToRecyclerView(binding.rvBanner);
                    dotAdapter = new DotAdapter(response.body().getBanner().size());
                    binding.rrcyclerDots.setAdapter(dotAdapter);

                }
            }

            @Override
            public void onFailure(Call<BannerRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }

        });
    }

    public void getHashtagVideo() {
        Call<HashtagVideoRoot> call = RetrofitBuilder.create().getHashtagVideo("ALL", start, Const.LIMIT);

        call.enqueue(new Callback<HashtagVideoRoot>() {
            @Override
            public void onResponse(Call<HashtagVideoRoot> call, Response<HashtagVideoRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getHashtag().isEmpty()) {
                        hashtagAllAdapter.addData(response.body().getHashtag());
                        isLoding = false;
                    } else if (start == 0) {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvAllHashtag.setVisibility(View.GONE);
                    }
                }
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }

            @Override
            public void onFailure(Call<HashtagVideoRoot> call, Throwable t) {
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }
        });
    }


}
