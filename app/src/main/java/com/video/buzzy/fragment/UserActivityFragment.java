package com.video.buzzy.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.video.buzzy.R;
import com.video.buzzy.activity.CommentActivity;
import com.video.buzzy.activity.FollowersActivity;
import com.video.buzzy.activity.GiftActivity;
import com.video.buzzy.activity.LikesActivity;
import com.video.buzzy.activity.MentionActivity;
import com.video.buzzy.adapter.UserActivitiesAdapter;
import com.video.buzzy.databinding.FragmentUserActivityBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;
import com.video.buzzy.modelRetrofit.RestResponse;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivityFragment extends Fragment {
    FragmentUserActivityBinding binding;
    UserActivitiesAdapter activitiesAdapter;
    NavController navController;
    SessionManager sessionManager;


    public UserActivityFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_activity, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        navController = Navigation.findNavController(requireActivity(), R.id.host);

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        sessionManager = new SessionManager(getActivity());

        activitiesAdapter = new UserActivitiesAdapter();
        binding.rvactvities.setAdapter(activitiesAdapter);

        activitiesAdapter.setOnClickDelete(notificationsItem -> {
            Log.d("TAG", "onResponse: success");
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.delete_popup);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Button yes = dialog.findViewById(R.id.yes);
            Button no = dialog.findViewById(R.id.no);

            yes.setOnClickListener(v1 -> {
                deleleNotification(notificationsItem.getId());
                activitiesAdapter.removeItem(notificationsItem);
                Toast.makeText(getActivity(), "Deleted successfully.....", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            no.setOnClickListener(v12 -> dialog.dismiss());

            dialog.show();

        });

        getAllNotification();


        binding.likes.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), LikesActivity.class);
            startActivity(i);

        });
        binding.comments.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CommentActivity.class);
            startActivity(i);

        });

        binding.followers.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), FollowersActivity.class);
            startActivity(i);
        });

        binding.mentions.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), MentionActivity.class);
            startActivity(i);

        });
        binding.gifts.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), GiftActivity.class);
            startActivity(i);
        });

        binding.swipeLayout.setOnRefreshListener(() -> {
            getAllNotification();
        });

    }

    public void getAllNotification() {
        Call<NotificationRoot> call = RetrofitBuilder.create().getTypeOfNotification("ALL", sessionManager.getUser().getId());
        call.enqueue(new Callback<NotificationRoot>() {
            @Override
            public void onResponse(Call<NotificationRoot> call, Response<NotificationRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getNotifications().isEmpty()) {
                        binding.nodataLay.setVisibility(View.GONE);
                        binding.rvactvities.setVisibility(View.VISIBLE);
                        activitiesAdapter.clear();
                        activitiesAdapter.addData(response.body().getNotifications());
                    } else {
                        binding.rvactvities.setVisibility(View.GONE);
                        binding.nodataLay.setVisibility(View.VISIBLE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
                binding.swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<NotificationRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.rvactvities.setVisibility(View.GONE);
                binding.nodataLay.setVisibility(View.VISIBLE);
            }
        });
    }

    public void deleleNotification(String id) {
        Call<RestResponse> call = RetrofitBuilder.create().deleteNotification(id);

        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.d("TAG", "onResponse: success");
                }
            }


            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


}
