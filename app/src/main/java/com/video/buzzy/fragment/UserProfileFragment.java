package com.video.buzzy.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.google.gson.JsonObject;
import com.video.buzzy.R;
import com.video.buzzy.activity.ChatActivity;
import com.video.buzzy.activity.UserFollowerActivity;
import com.video.buzzy.adapter.GiftProfileAdapter;
import com.video.buzzy.adapter.UserPostVideoAdapter;
import com.video.buzzy.databinding.FragmentUserProfileBinding;
import com.video.buzzy.modelRetrofit.AllVideoRoot;
import com.video.buzzy.modelRetrofit.GiftRoot;
import com.video.buzzy.modelRetrofit.RestResponse;
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

public class UserProfileFragment extends Fragment {
    FragmentUserProfileBinding binding;
    NavController navController;
    SessionManager sessionManager;
    GiftViewModel giftViewModel;
    List<UserRoot.GiftItem> userGift = new ArrayList<>();
    String userId;
    String username;
    String userid;
    private GiftProfileAdapter giftAdapter;
    private UserPostVideoAdapter userPostVideoAdapter;


    public UserProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        binding.profileLay.setVisibility(View.GONE);

        sessionManager = new SessionManager(getActivity());
        giftViewModel = ViewModelProviders.of(this).get(GiftViewModel.class);


        navController = Navigation.findNavController(requireActivity(), R.id.host);
        Intent intent = getActivity().getIntent();


        if (intent.getStringExtra(Const.USERID) != null && !intent.getStringExtra(Const.USERID).isEmpty()) {
            userId = intent.getStringExtra(Const.USERID);
        } else {
            username = getArguments().getString(Const.FINDUSERPROFILEBYNAME);
            userid = getArguments().getString(Const.USERIDFORGETPROFILE);

            if (username != null && !username.isEmpty()) {
                Log.d("TAG", "initView: " + username);
                getProfileByUsername(username);
            } else if (userid != null && !userid.isEmpty()) {
                userId = userid;
            }
        }
        userPostVideoAdapter = new UserPostVideoAdapter();

        binding.rvGift.setAdapter(giftViewModel.giftProfileAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.rvPostVideo.setLayoutManager(staggeredGridLayoutManager);
        binding.rvPostVideo.setAdapter(userPostVideoAdapter);

        getProfile();
        getReliteData();

        userPostVideoAdapter.setOnItemUserVideoClick((pos, reelItem) -> {
            Bundle b = new Bundle();
            b.putInt(Const.USERREELPOS, pos);
            b.putString(Const.VIDEOUSERID, new Gson().toJson(reelItem));
            b.putString(Const.REELUSER, userId);
            navController.navigate(R.id.action_userProfileFragment_to_reelsOfUserFragment, b);
        });

        binding.back.setOnClickListener(v -> {
            try {
                navController.popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        binding.btnFollow.setOnClickListener(v -> {
//            if (binding.btnFollow.getText().toString().equalsIgnoreCase("Follow")) {
//                binding.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_following));
//                binding.btnFollow.setText(R.string.following);
//            }
//        });

        binding.message.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ChatActivity.class);
            i.putExtra(Const.CHATUSERNAME, binding.username.getText().toString());
            i.putExtra(Const.CHATUSERId, userid);
            startActivity(i);
        });

        binding.btnFollow.setOnClickListener(v -> {
            binding.btnFollow.setVisibility(View.INVISIBLE);
            binding.loader.setVisibility(View.VISIBLE);
            startFollow();
        });


        binding.followerLay.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), UserFollowerActivity.class);
            i.putExtra(Const.OTHERUSERFOLLOWER, userid);
            i.putExtra(Const.FOLLOWERUSERNAME, binding.username.getText());
            startActivity(i);
        });

        binding.followingLay.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), UserFollowerActivity.class);
            i.putExtra(Const.OTHERUSERFOLLOWER, userid);
            i.putExtra(Const.FOLLOWERUSERNAME, binding.username.getText());
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
                                // Toast.makeText(getActivity(), "matched", Toast.LENGTH_SHORT).show();
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

        Call<UserRoot> call = RetrofitBuilder.create().getOtherUser(sessionManager.getUser().getId(), userId);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getUser() != null) {
                    Log.d("TAG", "onResponse: ");
                    binding.coverImag.setVisibility(View.VISIBLE);

                    if (response.body().getUser().getCoverImage() != null) {
                        Glide.with(binding.getRoot()).load(response.body().getUser().getCoverImage()).into(binding.coverImag);
                        Glide.with(binding.getRoot()).load(response.body().getUser().getCoverImage()).into(binding.backgroundView);

//                        Bitmap bitmap = BitmapFactory.decodeFile(response.body().getUser().getCoverImage());
//                        BitmapDrawable background = new BitmapDrawable(getResources(), bitmap);
//                        binding.scrollView.setBackgroundDrawable(background);
//                        binding.scrollView.setBackground(ContextCompat.getDrawable(getActivity(), Integer.parseInt(response.body().getUser().getCoverImage())));

                        //binding.scrollView.setBackgroundColor(getResources().getColor(R.color.transparent));

                    } else {
                        binding.coverImag.setImageResource(0);
                        Glide.with(binding.getRoot()).load(R.drawable.shape_dark_blue).into(binding.backgroundView);
//                        binding.scrollView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_dark_blue));
                        //binding.scrollView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }

                    setData(response.body().getUser(), response.body().getUser().getGift());
                }
                binding.profileLay.setVisibility(View.VISIBLE);

                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }
        });
    }


    public void getProfileByUsername(String username) {
        Call<UserRoot> call = RetrofitBuilder.create().getProfileByUsername(sessionManager.getUser().getId(), username);

        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getUser() != null) {
                    setData(response.body().getUser(), response.body().getUser().getGift());
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {

            }
        });
    }


    public void setData(UserRoot.User user, List<UserRoot.GiftItem> giftItem) {
        if (user.getId().equals(sessionManager.getUser().getId())) {
            binding.followchatLay.setVisibility(View.GONE);
        } else {
            binding.followchatLay.setVisibility(View.VISIBLE);
            binding.followchatLay.setVisibility(View.VISIBLE);
        }

        binding.name.setText(user.getName());
        binding.username.setText(user.getUsername());
        binding.bio.setText(user.getBio());
        binding.followerscount.setText(String.valueOf(user.getFollowers()));
        binding.followingcount.setText(String.valueOf(user.getFollowing()));
        binding.diamond.setText(String.valueOf(user.getDiamond()));

        if (user.isFollow()) {
            binding.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_following));
            binding.btnFollow.setText(R.string.following);
        } else {
            binding.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_lightblue));
            binding.btnFollow.setText(R.string.follow);
        }


        if (!user.getGift().isEmpty()) userGift = giftItem;

//                    if (user.getCoverImage() != null)
//                        Glide.with(binding.getRoot()).load(user.getCoverImage()).into(binding.coverImg);

        //  Glide.with(binding.getRoot()).load(user.getProfileImage()).into(binding.profilePic);

        Glide.with(binding.getRoot()).load(user.getProfileImage())
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
                }).into(binding.profilePic);

//        if (!user.getReel().isEmpty()) {
//            Log.d("=======", "setData: empty not ");
//            userPostVideoAdapter.addData(user.getReel());
//
//            Log.d("=======", "setData: " + user.getReel().size());
//
//            binding.nodataLay.setVisibility(View.INVISIBLE);
//        } else {
//            Log.d("=======", "setData: empty");
//            binding.nodataLay.setVisibility(View.VISIBLE);
//            binding.rvPostVideo.setVisibility(View.GONE);
//        }

        getGift();
    }

    @Override
    public void onResume() {
        //getProfile();
        Log.d("TAG", "onResume: profile");
        super.onResume();
    }

    public void startFollow() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from", sessionManager.getUser().getId());
        jsonObject.addProperty("to", userId);

        Call<RestResponse> call = RetrofitBuilder.create().setFollowOrUnfollow(jsonObject);

        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.d("TAG", "onResponse: ");
                    if (response.body().isFollow()) {
                        binding.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_following));
                        binding.btnFollow.setText(R.string.following);
                    } else {
                        binding.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_lightblue));
                        binding.btnFollow.setText(R.string.follow);
                    }
                    binding.btnFollow.setVisibility(View.VISIBLE);
                    binding.loader.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.btnFollow.setVisibility(View.VISIBLE);
                binding.loader.setVisibility(View.GONE);
            }
        });
    }

    public void getReliteData() {

        Call<AllVideoRoot> call = RetrofitBuilder.create().getUserWiseReels(userId);
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


}
