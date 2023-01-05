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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.FragmentPendingBinding;
import com.video.buzzy.modelRetrofit.RedeeHistoryRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.HistoryViewModel;
import com.video.buzzy.viewmodel.ViewModelFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragment extends Fragment {
    FragmentPendingBinding binding;
    HistoryViewModel viewModel;
    SessionManager sessionManager;
    boolean isLoding = false;
    Call<RedeeHistoryRoot> call;
    private int start = 0;


    public PendingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending, container, false);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new HistoryViewModel()).createFor()).get(HistoryViewModel.class);
        initData();
        return binding.getRoot();
    }

    private void initData() {

        sessionManager = new SessionManager(getActivity());
        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();
        binding.rvPendingHistory.setVisibility(View.GONE);


        binding.rvPendingHistory.setAdapter(viewModel.historyAdapter);
        viewModel.historyAdapter.setTextHistory("pending");

        getHistory();


        binding.rvPendingHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        getHistory();
                    }
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void getHistory() {

        if (call != null) {
            call.cancel();
        }


        call = RetrofitBuilder.create().getRedeemHistory(sessionManager.getUser().getId(), "pending", start, Const.LIMIT);

        call.enqueue(new Callback<RedeeHistoryRoot>() {
            @Override
            public void onResponse(Call<RedeeHistoryRoot> call, Response<RedeeHistoryRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getRedeem().isEmpty()) {
                        Log.d("TAG", "onResponse: success " + response.body().getRedeem().size());
                        binding.nodataLay.setVisibility(View.GONE);
                        binding.rvPendingHistory.setVisibility(View.VISIBLE);
                        viewModel.historyAdapter.clear();
                        viewModel.historyAdapter.addData(response.body().getRedeem());
                    } else if (start == 0) {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvPendingHistory.setVisibility(View.GONE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
            }

            @Override
            public void onFailure(Call<RedeeHistoryRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvPendingHistory.setVisibility(View.GONE);
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
            }
        });
    }
}
