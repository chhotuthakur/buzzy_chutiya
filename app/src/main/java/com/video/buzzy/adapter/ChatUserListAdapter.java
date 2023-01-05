package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemChatUserListBinding;
import com.video.buzzy.modelRetrofit.ChatThumbList;

import java.util.ArrayList;
import java.util.List;

public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.HashtagHolder> {
    List<ChatThumbList.Chat> userlist = new ArrayList<>();
    OnItemClick onItemClick;
    Context context;

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HashtagHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public List<ChatThumbList.Chat> getList() {
        return userlist;
    }

    public void addData(List<ChatThumbList.Chat> user) {
        this.userlist.addAll(user);
        notifyItemRangeInserted(this.userlist.size(), user.size());
    }


    public interface OnItemClick {
        void onClick(ChatThumbList.Chat chat);

        void onProfile(int pos);
    }

    public class HashtagHolder extends RecyclerView.ViewHolder {
        ItemChatUserListBinding binding;

        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemChatUserListBinding.bind(itemView);
        }

        public void setData(int position) {
            ChatThumbList.Chat chatUser = userlist.get(position);

            if (userlist.get(position).isOnline()) {
                binding.onlineView.setVisibility(View.VISIBLE);
                binding.offlineView.setVisibility(View.GONE);
                binding.msgLay.setVisibility(View.VISIBLE);
            } else {
                binding.onlineView.setVisibility(View.GONE);
                binding.offlineView.setVisibility(View.VISIBLE);
                binding.msgLay.setVisibility(View.GONE);
            }

            Glide.with(binding.getRoot()).load(chatUser.getUserImage()).circleCrop().into(binding.userImg);
            binding.name.setText(chatUser.getName());
            binding.time.setText(chatUser.getTime());
            binding.lastMsg.setText(chatUser.getLastMsg());

            binding.chatName.setOnClickListener(v -> {
                onItemClick.onClick(chatUser);
            });

            binding.profile.setOnClickListener(v -> {
                onItemClick.onProfile(position);
            });


        }

    }
}
