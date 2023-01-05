package com.video.buzzy.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.video.buzzy.R;
import com.video.buzzy.activity.ChatActivity;
import com.video.buzzy.activity.MainPageActivity;
import com.video.buzzy.adapter.ChatUserListAdapter;
import com.video.buzzy.databinding.FragmentChatBinding;
import com.video.buzzy.modelRetrofit.ChatThumbList;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    public static boolean isfromrecharge = false;
    FragmentChatBinding binding;
    ChatUserListAdapter chatUserListAdapter;
    int postion;
    NavController navController;
    SessionManager sessionManager;

    public ChatFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        initView();
        return binding.getRoot();

    }

    private void initView() {

        sessionManager = new SessionManager(getActivity());

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        chatUserListAdapter = new ChatUserListAdapter();
        binding.rvChatUser.setAdapter(chatUserListAdapter);

        getThumblist();

        navController = Navigation.findNavController(requireActivity(), R.id.host);

        chatUserListAdapter.setOnItemClick(new ChatUserListAdapter.OnItemClick() {
            @Override
            public void onClick(ChatThumbList.Chat chat) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                i.putExtra(Const.CHATUSERNAME, chat.getName());
                i.putExtra(Const.CHATUSERId, chat.getUserId());
                startActivity(i);
            }

            @Override
            public void onProfile(int pos) {
//                Bundle b = new Bundle();
//                b.putString(Const.USERIMAGE, Demo_contents.getChatUsers().get(pos).getImg());
//                b.putString(Const.USERNAMELIST, Demo_contents.getChatUsers().get(pos).getUsername());
//                navController.navigate(R.id.action_chatfragment_to_userProfileFragment, b);
            }
        });

    }

    public void openRecharge() {
        navController.navigate(R.id.action_chatFragment_to_walletFragment);
    }


    public void getThumblist() {
        Call<ChatThumbList> call = RetrofitBuilder.create().getChatThumbList(sessionManager.getUser().getId());

        call.enqueue(new Callback<ChatThumbList>() {
            @Override
            public void onResponse(Call<ChatThumbList> call, Response<ChatThumbList> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getChat().isEmpty()) {
                        binding.rvChatUser.setVisibility(View.VISIBLE);
                        binding.nodata.setVisibility(View.GONE);
                        chatUserListAdapter.addData(response.body().getChat());
                    } else {
                        binding.nodata.setVisibility(View.VISIBLE);
                        binding.rvChatUser.setVisibility(View.GONE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
            }

            @Override
            public void onFailure(Call<ChatThumbList> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.nodata.setVisibility(View.VISIBLE);
                binding.rvChatUser.setVisibility(View.GONE);
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isfromrecharge) {
            ((MainPageActivity) getActivity()).openRecharge();
        }
    }
}
