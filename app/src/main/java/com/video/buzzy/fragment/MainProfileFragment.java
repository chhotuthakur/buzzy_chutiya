package com.video.buzzy.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.video.buzzy.R;
import com.video.buzzy.activity.EditActivity;
import com.video.buzzy.activity.HistoryActivity;
import com.video.buzzy.activity.SettingActivity;
import com.video.buzzy.activity.UserFollowerActivity;
import com.video.buzzy.adapter.UserPostVideoAdapter;
import com.video.buzzy.databinding.FragmentMainProfileBinding;
import com.video.buzzy.modelRetrofit.AllVideoRoot;
import com.video.buzzy.modelRetrofit.GiftRoot;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.GiftViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainProfileFragment extends Fragment {
    FragmentMainProfileBinding binding;
    UserPostVideoAdapter userPostVideoAdapter;
    NavController navController;
    GiftViewModel giftViewModel;
    SessionManager sessionManager;
    List<UserRoot.GiftItem> userGift = new ArrayList<>();

    public MainProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();
        binding.profileLay.setVisibility(View.GONE);

        giftViewModel = ViewModelProviders.of(this).get(GiftViewModel.class);
        userPostVideoAdapter = new UserPostVideoAdapter();

        sessionManager = new SessionManager(getActivity());

        //getProfile();

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        // setting recycler view layout to staggered grid

        binding.rvPostVideo.setLayoutManager(staggeredGridLayoutManager);
        binding.rvPostVideo.setAdapter(userPostVideoAdapter);


        binding.rvGift.setAdapter(giftViewModel.giftProfileAdapter);
        navController = Navigation.findNavController(requireActivity(), R.id.host);


        getReliteData();

        userPostVideoAdapter.setOnItemUserVideoClick((pos, reelItem) -> {
            Bundle b = new Bundle();
            b.putInt(Const.USERREELPOS, pos);
            b.putString(Const.VIDEOUSERID, new Gson().toJson(reelItem));
            b.putString(Const.REELUSER, sessionManager.getUser().getId());

            navController.navigate(R.id.action_userProfileFragment_to_reelsOfUserFragment, b);

        });

        binding.edit.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), EditActivity.class);
            startActivity(i);
        });

        binding.followerLay.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), UserFollowerActivity.class);
            startActivity(i);
        });

        binding.followingLay.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), UserFollowerActivity.class);
            startActivity(i);
        });

        binding.wallet.setOnClickListener(v -> {
            navController.navigate(R.id.action_mainProfileFragment_to_walletFragment);
        });

        binding.setting.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SettingActivity.class);
            startActivity(i);
        });

        binding.history.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), HistoryActivity.class);
            startActivity(i);
        });

    }

    private void getGift() {
        Call<GiftRoot> call = RetrofitBuilder.create().getAllGift("all");

        call.enqueue(new Callback<GiftRoot>() {
            @Override
            public void onResponse(Call<GiftRoot> call, Response<GiftRoot> response) {
                Log.d("TAG", "onResponse: ");
                if (response.code() == 200 && response.isSuccessful() && !response.body().getGift().isEmpty()) {
                    Log.d("TAG", "onResponse: " + response.body().getGift().toString());

                    giftViewModel.giftProfileAdapter.clear();

                    for (int i = 0; i < response.body().getGift().size(); i++) {
                        for (int j = 0; j < userGift.size(); j++) {
                            Log.d("TAG", "onResponse: ");
                            if (userGift.get(j).getGift().equals(response.body().getGift().get(i).getId())) {
                                response.body().getGift().get(i).setReceivedcount(userGift.get(j).getCount());
                            }
                        }
                    }

                    giftViewModel.giftProfileAdapter.addData(response.body().getGift());
                }
            }

            @Override
            public void onFailure(Call<GiftRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }

        });

    }


    public void getProfile() {
        Call<UserRoot> call = RetrofitBuilder.create().getProfile(sessionManager.getUser().getId());

        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getUser() != null) {

                    if (response.body().getUser().isIsBlock()) {
                        try {
                            Toast.makeText(getActivity(), "You are blocked by admin", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    binding.profileLay.setVisibility(View.VISIBLE);

                    Log.d("TAG", "onResponse: ");
                    UserRoot.User user = response.body().getUser();

                    userPostVideoAdapter.clear();

                    if (user.getName().isEmpty()) {
                        binding.name.setText("Buzzy User");
                    } else {
                        binding.name.setText(user.getName());
                    }
                    binding.username.setText(user.getUsername());
                    binding.bio.setText(user.getBio());
                    binding.followersCount.setText(String.valueOf(user.getFollowers()));
                    binding.folllowingCount.setText(String.valueOf(user.getFollowing()));
                    binding.diamond.setText(String.valueOf(user.getDiamond()));

                    if (!user.getGift().isEmpty()) userGift = response.body().getUser().getGift();

                    Log.d("TAG", "onResponse: " + user.getProfileImage());

                    Glide.with(binding.getRoot()).load(response.body().getUser().getProfileImage())
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .error(R.drawable.shape_dark_blue)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(binding.profileImage);

                    //Glide.with(binding.getRoot()).load(response.body().getUser().getProfileImage()).into(binding.profileImage);



                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();

                    getGift();
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvPostVideo.setVisibility(View.GONE);
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }
        });

    }

    public void getReliteData() {

        Call<AllVideoRoot> call = RetrofitBuilder.create().getUserWiseReels(sessionManager.getUser().getId());
        call.enqueue(new Callback<AllVideoRoot>() {
            @Override
            public void onResponse(Call<AllVideoRoot> call, Response<AllVideoRoot> response) {
                if (response.code() == 200) {

                    if (response.code() == 200 && response.isSuccessful()) {
                        Log.d("<<>>>>", "onResponse: ");

                        if (!response.body().getReel().isEmpty()) {
                            Log.d("<<>>>>", "onResponse: ");
                            binding.nodataLay.setVisibility(View.GONE);
                            binding.rvPostVideo.setVisibility(View.VISIBLE);
                            userPostVideoAdapter.clear();
                            userPostVideoAdapter.addData(response.body().getReel());
                            binding.nodataLay.setVisibility(View.GONE);
                        } else {
                            Log.d("<<>>>>", "onResponse: ");
                            binding.nodataLay.setVisibility(View.VISIBLE);
                            binding.rvPostVideo.setVisibility(View.GONE);
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<AllVideoRoot> call, Throwable t) {
                Log.d("<<>>>>", "onFailure: " + t.getMessage());

            }
        });
    }

    @Override
    public void onResume() {
        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();
        binding.profileLay.setVisibility(View.INVISIBLE);
        getProfile();

        Log.d(">>>>>>>", "onResume: profile");
        super.onResume();
    }
}
