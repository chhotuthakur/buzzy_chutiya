package com.video.buzzy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.video.buzzy.R;
import com.video.buzzy.adapter.FollowersUserAdapter;
import com.video.buzzy.databinding.FragmentUserFollowersBinding;
import com.video.buzzy.databinding.ItemFollowersListBinding;
import com.video.buzzy.design.SuggestedUser;
import com.video.buzzy.modelRetrofit.RestResponse;
import com.video.buzzy.modelRetrofit.UserFollowersRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFollowersFragment extends Fragment {
    FragmentUserFollowersBinding binding;
    FollowersUserAdapter followersUserAdapter;
    int pos;
    List<SuggestedUser> suggestedUsers = new ArrayList<>();
    SessionManager sessionManager;
    String name = "";
    private Call<UserFollowersRoot> call;
    boolean isLoding = false;
    private int start = 0;
    String user_id;

    public UserFollowersFragment(String userid) {
        this.user_id = userid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_followers, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        followersUserAdapter = new FollowersUserAdapter();
        sessionManager = new SessionManager(getActivity());
        binding.rvFollowers.setAdapter(followersUserAdapter);

        getFollowers();

        binding.rvFollowers.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        getFollowers();
                    }
                }
            }
        });

        followersUserAdapter.setOnClickFollow((userItem, pos, binding) -> {
            userItem.setFollow(true);
            binding.loader.setVisibility(View.VISIBLE);
            binding.btnFollow.setVisibility(View.INVISIBLE);
            UserFollowersFragment.this.startFollow(userItem.getUserId(), userItem, binding);
            followersUserAdapter.notifyItemChanged(pos, userItem);
        });

        binding.swipeLayout.setOnRefreshListener(() -> {
            followersUserAdapter.clear();
            getFollowers();
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (call != null) {
                    call.cancel();
                }

                binding.rvFollowers.setVisibility(View.GONE);
                name = cs.toString();
                getFollowers();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {

                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);

                if (call != null) {
                    call.cancel();
                }

                binding.etSearch.clearFocus();
                name = binding.etSearch.getText().toString();
                getFollowers();


            }
            return true;
        });


    }


    public void getFollowers() {
        Log.d("======", "searchPost: " + name);

        if (call != null) {
            call.cancel();
        }

        if (name.isEmpty()) {
            binding.nodataLay.setVisibility(View.GONE);
            followersUserAdapter.clear();
        }

        if (user_id != null && !user_id.isEmpty()) {
            user_id = user_id;
        } else {
            user_id = sessionManager.getUser().getId();
        }


        call = RetrofitBuilder.create().getFollowersList(user_id, name, start, Const.LIMIT);

        call.enqueue(new Callback<UserFollowersRoot>() {
            @Override
            public void onResponse(Call<UserFollowersRoot> call, Response<UserFollowersRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && !response.body().getUser().isEmpty()) {
                    Log.d("TAG", "onResponse: ");
                    binding.rvFollowers.setVisibility(View.VISIBLE);
                    binding.nodataLay.setVisibility(View.GONE);
                    followersUserAdapter.clear();
                    followersUserAdapter.addData(response.body().getUser());
                    isLoding = false;
                } else if (start == 0) {
                    binding.nodataLay.setVisibility(View.VISIBLE);
                    binding.rvFollowers.setVisibility(View.GONE);
                }
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();

                binding.swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserFollowersRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvFollowers.setVisibility(View.GONE);
            }

        });
    }

    public void startFollow(String otherUserId, UserFollowersRoot.UserItem userItem, ItemFollowersListBinding binding) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from", sessionManager.getUser().getId());
        jsonObject.addProperty("to", otherUserId);

        Call<RestResponse> call = RetrofitBuilder.create().setFollowOrUnfollow(jsonObject);

        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.d("TAG", "onResponse: ");
                    if (response.body().getMessage().equalsIgnoreCase("User Followed successfully!!")) {
                        userItem.setFollow(true);
                        binding.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_following));
                        binding.btnFollow.setText(R.string.following);
                    } else if (response.body().getMessage().equalsIgnoreCase("User unFollowed successfully!!")) {
                        userItem.setFollow(false);
                        binding.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_lightblue));
                        binding.btnFollow.setText(R.string.follow);
                    }

                }
                binding.loader.setVisibility(View.GONE);
                binding.btnFollow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.loader.setVisibility(View.GONE);
                binding.btnFollow.setVisibility(View.VISIBLE);
            }
        });
    }
}
