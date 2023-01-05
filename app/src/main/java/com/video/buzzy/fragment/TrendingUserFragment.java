package com.video.buzzy.fragment;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.video.buzzy.R;
import com.video.buzzy.adapter.TrendingUserAdapter;
import com.video.buzzy.databinding.FragmentTrendingUserBinding;
import com.video.buzzy.databinding.ItemFollowingListBinding;
import com.video.buzzy.modelRetrofit.RestResponse;
import com.video.buzzy.modelRetrofit.TrendingUserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingUserFragment extends BaseFragment {

    FragmentTrendingUserBinding binding;
    TrendingUserAdapter suggstedUserAdapter;
    TransitionInflater inflater;
    NavController navController;
    SessionManager sessionManager;
    private TrendingUserRoot.UserItem suggestedUserRoot;
    boolean isLoding = false;
    private int start = 0;


    public TrendingUserFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending_user, container, false);
        initView();

        return binding.getRoot();
    }


    private void initView() {
        navController = Navigation.findNavController(requireActivity(), R.id.host);

        sessionManager = new SessionManager(getActivity());

        suggstedUserAdapter = new TrendingUserAdapter();
        binding.rvSuggestedUser.setAdapter(suggstedUserAdapter);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        getTrendingUser();

        binding.rvSuggestedUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        getTrendingUser();
                    }
                }
            }
        });


        inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));

        binding.back.setOnClickListener(v -> {
            navController.popBackStack();
        });


        binding.swipeLayout.setOnRefreshListener(() -> {
            suggstedUserAdapter.clear();
            getTrendingUser();
        });

        suggstedUserAdapter.setOnClickUser(new TrendingUserAdapter.OnClickUser() {
            @Override
            public void onClick(TrendingUserRoot.UserItem suggestedUser) {
                suggestedUserRoot = suggestedUser;
                Bundle bundle = new Bundle();
                bundle.putString(Const.USERIDFORGETPROFILE, suggestedUser.getUserId());
                navController.navigate(R.id.action_searchUserHashFragment_to_userProfileFragment, bundle);
            }

            @Override
            public void OnClickFollow(TrendingUserRoot.UserItem userItem, ItemFollowingListBinding binding) {
                binding.loader.setVisibility(View.VISIBLE);
                binding.btnFollow.setVisibility(View.INVISIBLE);
                startFollow(userItem.getUserId(), binding);
            }
        });

    }

    public void getTrendingUser() {
        Call<TrendingUserRoot> call = RetrofitBuilder.create().getTrendingUser(sessionManager.getUser().getId(), start, Const.LIMIT);

        call.enqueue(new Callback<TrendingUserRoot>() {
            @Override
            public void onResponse(Call<TrendingUserRoot> call, Response<TrendingUserRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getUser().isEmpty()) {
                        Log.d("TAG", "onResponse: ");
                        suggstedUserAdapter.addData(response.body().getUser());
                        binding.nodataLay.setVisibility(View.GONE);
                        binding.rvSuggestedUser.setVisibility(View.VISIBLE);
                        isLoding = false;
                    } else if (start == 0) {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvSuggestedUser.setVisibility(View.GONE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
                binding.swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<TrendingUserRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.swipeLayout.setRefreshing(false);
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvSuggestedUser.setVisibility(View.GONE);
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }

        });

    }

    public void startFollow(String otherUserId, ItemFollowingListBinding binding1) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from", sessionManager.getUser().getId());
        jsonObject.addProperty("to", otherUserId);

        Call<RestResponse> call = RetrofitBuilder.create().setFollowOrUnfollow(jsonObject);

        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.d("TAG", "onResponse: ");

                    if (response.body().isFollow()) {
                        binding1.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_following));
                        binding1.btnFollow.setText(R.string.following);
                    } else {
                        binding1.btnFollow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_lightblue));
                        binding1.btnFollow.setText(R.string.follow);
                    }
                }

                binding1.loader.setVisibility(View.GONE);
                binding1.btnFollow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


}
