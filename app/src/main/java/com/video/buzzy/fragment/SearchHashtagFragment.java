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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.FragmentSearchHashtagBinding;
import com.video.buzzy.design.Hashtag;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.viewmodel.SearchUserHashModel;
import com.video.buzzy.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchHashtagFragment extends Fragment {
    FragmentSearchHashtagBinding binding;
    NavController navController;
    SearchUserHashModel viewModel;
    boolean ishash = false;
    Call<HashtagVideoRoot> call;
    private List<Hashtag> hash = new ArrayList<>();
    private int start = 0;
    private boolean isLoding = false;

    public SearchHashtagFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_hashtag, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        navController = Navigation.findNavController(requireActivity(), R.id.host);

        viewModel = ViewModelProviders.of(getParentFragment(), new ViewModelFactory(new SearchUserHashModel()).createFor()).get(SearchUserHashModel.class);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        binding.rvHashtag.setAdapter(viewModel.hashtagListAdapter);

        viewModel.hashtagListAdapter.clear();
        searchHashtag("ALL");


        viewModel.isUser.observe(getActivity(), aBoolean -> {
            ishash = aBoolean;
        });

        viewModel.searchString.observe(getActivity(), s -> {
            if (!ishash) {
                Log.d("TAG", "initView: " + s);
                if (!s.isEmpty()) {
                    searchHashtag(s);
                } else {
                    viewModel.hashtagListAdapter.clear();
                    searchHashtag("ALL");
                }
            }
        });


        viewModel.hashtagListAdapter.setOnItemClick(str -> {
            Bundle bundle = new Bundle();
            bundle.putString(Const.SEARCHHASHTAGTEXT, str.getHashtag());
            navController.navigate(R.id.action_searchUserHashFragment_to_viewHashtag, bundle);

        });

        binding.rvHashtag.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        searchHashtag("ALL");
                    }
                }
            }
        });


    }

    private void searchHashtag(String text) {
//        viewModel.hashtagListAdapter.clear();
//        List<Hashtag> finelUser = new ArrayList<>();
//        for (Hashtag u : hash) {
//            if (u.getHashtagtext().toLowerCase().contains(toString.toLowerCase())) {
//                finelUser.add(u);
//            }
//
//        }
//        viewModel.hashtagListAdapter.addData(finelUser);

        if (call != null) {
            call.cancel();
        }

        call = RetrofitBuilder.create().getHashtagVideo(text, start, Const.LIMIT);

        call.enqueue(new Callback<HashtagVideoRoot>() {
            @Override
            public void onResponse(Call<HashtagVideoRoot> call, Response<HashtagVideoRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && !response.body().getHashtag().isEmpty()) {
                    binding.rvHashtag.setVisibility(View.VISIBLE);
                    binding.nodataLay.setVisibility(View.GONE);
                    viewModel.hashtagListAdapter.clear();
                    viewModel.hashtagListAdapter.addData(response.body().getHashtag());
                } else {
                    binding.rvHashtag.setVisibility(View.VISIBLE);
                    binding.nodataLay.setVisibility(View.GONE);
                }

                if (!text.isEmpty() && response.body().getHashtag().isEmpty()) {
                    Log.d("TAG", "onResponse: hashtag empty");
                    binding.rvHashtag.setVisibility(View.GONE);
                }

                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();

            }

            @Override
            public void onFailure(Call<HashtagVideoRoot> call, Throwable t) {
                binding.rvHashtag.setVisibility(View.GONE);
                binding.nodataLay.setVisibility(View.VISIBLE);
            }

        });

    }

}
