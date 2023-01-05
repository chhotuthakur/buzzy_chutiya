package com.video.buzzy.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.JsonObject;
import com.video.buzzy.R;
import com.video.buzzy.adapter.UserListAdapter;
import com.video.buzzy.databinding.FragmentSearchUserBinding;
import com.video.buzzy.design.UserList;
import com.video.buzzy.modelRetrofit.SearchUserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.SearchUserHashModel;
import com.video.buzzy.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserFragment extends Fragment {
    FragmentSearchUserBinding binding;
    NavController navController;
    SearchUserHashModel searchUserHashModel;
    boolean isuser = false;
    private List<UserList> user = new ArrayList<>();
    private SessionManager sessionManager;
    private Call<SearchUserRoot> call;
    boolean isLoding = false;
    private int start = 0;


    public SearchUserFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false);
        initData();
        return binding.getRoot();
    }

    private void initData() {
        navController = Navigation.findNavController(requireActivity(), R.id.host);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        sessionManager = new SessionManager(getActivity());

        searchUserHashModel = ViewModelProviders.of(getParentFragment(), new ViewModelFactory(new SearchUserHashModel()).createFor()).get(SearchUserHashModel.class);

        binding.rvSearchUser.setAdapter(searchUserHashModel.userListAdapter);


        searchUserHashModel.userListAdapter.clear();
        searchUser("");

        searchUserHashModel.isUser.observe(getActivity(), aBoolean -> {
            isuser = aBoolean;
        });

        searchUserHashModel.searchString.observe(getActivity(), s -> {
            Log.d("TAG", "initData: search " + s);
            if (isuser) {
                if (!s.isEmpty()) {
                    searchUser(s);
                } else {
                    searchUserHashModel.userListAdapter.clear();
                    searchUser("");
                }
            }
        });


        searchUserHashModel.userListAdapter.setOnItemClick(new UserListAdapter.OnItemUserClick() {
            @Override
            public void onClick(SearchUserRoot.SearchItem userList) {
                Bundle bundle = new Bundle();
                bundle.putString(Const.USERIDFORGETPROFILE, userList.getId());
                // bundle.putParcelable(Const.TRENDUSER, userList);
                navController.navigate(R.id.action_searchUserHashFragment_to_userProfileFragment, bundle);
            }
        });


    }

    private void searchUser(String text) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", sessionManager.getUser().getId());
        jsonObject.addProperty("search", text);

        if (call != null) {
            call.cancel();
        }

        call = RetrofitBuilder.create().searchUser(jsonObject);
        call.enqueue(new Callback<SearchUserRoot>() {
            @Override
            public void onResponse(Call<SearchUserRoot> call, Response<SearchUserRoot> response) {
                if (response.code() == 200 && response.body().isStatus()) {
                    if (!response.body().getSearch().isEmpty()) {
                        binding.rvSearchUser.setVisibility(View.VISIBLE);
                        binding.nodataLay.setVisibility(View.GONE);
                        searchUserHashModel.userListAdapter.clear();
                        searchUserHashModel.userListAdapter.addData(response.body().getSearch());
                    } else {
                        binding.rvSearchUser.setVisibility(View.GONE);
                        binding.nodataLay.setVisibility(View.VISIBLE);
                    }

                    if (!text.isEmpty() && response.body().getSearch().isEmpty()) {
                        Log.d("TAG", "onResponse: user empty");
                        binding.rvSearchUser.setVisibility(View.GONE);

                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
            }

            @Override
            public void onFailure(Call<SearchUserRoot> call, Throwable t) {
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                binding.rvSearchUser.setVisibility(View.GONE);
                binding.nodataLay.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
