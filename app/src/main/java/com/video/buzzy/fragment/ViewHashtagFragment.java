package com.video.buzzy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.activity.RecorderActivity;
import com.video.buzzy.adapter.ViewHashtagAdapter;
import com.video.buzzy.databinding.FragmentViewHashtagBinding;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewHashtagFragment extends Fragment {
    FragmentViewHashtagBinding binding;
    ViewHashtagAdapter viewHashtagAdapter;
    NavController controller;
    String hashtagName;
    private int start = 0;
    private boolean isLoding = false;

    public ViewHashtagFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_hashtag, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.shootBelow.setVisibility(View.GONE);
        controller = Navigation.findNavController(requireActivity(), R.id.host);

        hashtagName = getArguments().getString(Const.SEARCHHASHTAGTEXT);
        binding.hashtagName.setText(hashtagName);

        viewHashtagAdapter = new ViewHashtagAdapter();
        binding.rvViewHashtag.setAdapter(viewHashtagAdapter);

        getHashtagVideo();

        viewHashtagAdapter.setOnHashtagVideoClick((List<HashtagVideoRoot.ReelItem> reelItem, int pos) -> {
            Bundle b = new Bundle();
            b.putString(Const.REELSDATA, new Gson().toJson(reelItem));
            b.putInt(Const.REELPOS, pos);
            controller.navigate(R.id.action_searchFragment_to_reelsFragment, b);
        });


        if (binding.scrollView != null) {
            binding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY > oldScrollY)
                    Animatoo.animateCard(requireActivity());
                if (scrollY < oldScrollY) Animatoo.animateCard(requireActivity());
            });
        }

        binding.appBar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener)
                (appBarLayout, verticalOffset) -> {
                    if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                        // If collapsed, then do this

                        binding.shootBelow.setVisibility(View.VISIBLE);
                        binding.iconSmall.setVisibility(View.VISIBLE);
                        binding.bgImg.setVisibility(View.VISIBLE);
                        binding.txtToolbar.setVisibility(View.VISIBLE);
                        Animatoo.animateCard(requireActivity());
                        binding.toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_color));

                    } else if (verticalOffset == 0) {
                        // If expanded, then do this
                        binding.shootBelow.setVisibility(View.GONE);
                        binding.iconSmall.setVisibility(View.GONE);
                        binding.bgImg.setVisibility(View.GONE);
                        binding.txtToolbar.setVisibility(View.GONE);
                        binding.toolbar.setBackground(null);
                        Animatoo.animateCard(requireActivity());

                    } else {
                        // Somewhere in between
                        // Do according to your requirement
                    }

                });

        binding.back.setOnClickListener(v -> {
            controller.popBackStack();
        });

        binding.shootAbove.setOnClickListener(v -> {
            Intent i1 = new Intent(getActivity(), RecorderActivity.class);
            startActivity(i1);
        });

        binding.shootBelow.setOnClickListener(v -> {
            Intent i2 = new Intent(getActivity(), RecorderActivity.class);
            startActivity(i2);
        });


        binding.rvViewHashtag.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    public void getHashtagVideo() {
        Log.d(">>>>", "getHashtagVideo: " + hashtagName);

        Call<HashtagVideoRoot> call = RetrofitBuilder.create().getHashtagVideo(hashtagName, start, Const.LIMIT);

        call.enqueue(new Callback<HashtagVideoRoot>() {
            @Override
            public void onResponse(Call<HashtagVideoRoot> call, Response<HashtagVideoRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {

                    if (!response.body().getHashtag().isEmpty()) {
                        binding.rvViewHashtag.setVisibility(View.VISIBLE);
                        binding.nodataLay.setVisibility(View.GONE);

                        for (int i = 0; i < response.body().getHashtag().size(); i++) {
                            HashtagVideoRoot.HashtagItem hashtag = response.body().getHashtag().get(i);
                            viewHashtagAdapter.addData(hashtag.getReel());

                            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + hashtag.getImage()).into(binding.hashtagImg);
                            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + hashtag.getImage()).into(binding.hashtagImg1);
                            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + hashtag.getCoverImage()).into(binding.coverImage);
                            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + hashtag.getCoverImage()).into(binding.bgImg);

                            binding.hashtagName.setText(hashtag.getHashtag());
                            binding.totalLikes.setText(String.valueOf(hashtag.getLikes()));
                            binding.totalComment.setText(String.valueOf(hashtag.getComments()));
                            binding.totalVideo.setText(String.valueOf(hashtag.getVideoCount()));
                            binding.description.setText(hashtag.getDescription());

                            Log.d(">>>>>>", "onResponse: " + response.body().getHashtag().get(i).getReel().size());
                        }
                    }
                    Log.d(">>>>>>", "onResponse: " + response.body().getHashtag().size());
                } else {
                    binding.nodataLay.setVisibility(View.VISIBLE);
                    binding.rvViewHashtag.setVisibility(View.GONE);
                }

                binding.shimmar.stopShimmer();
                binding.shimmar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HashtagVideoRoot> call, Throwable t) {
                binding.shimmar.stopShimmer();
                binding.shimmar.setVisibility(View.GONE);
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvViewHashtag.setVisibility(View.GONE);
            }
        });
    }


}

