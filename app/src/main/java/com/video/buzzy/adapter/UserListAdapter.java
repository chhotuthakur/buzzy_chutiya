package com.video.buzzy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemSearchUserBinding;
import com.video.buzzy.modelRetrofit.SearchUserRoot;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserHolder> {
    List<SearchUserRoot.SearchItem> userlist = new ArrayList<>();

    OnItemUserClick onItemClick;

    public OnItemUserClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemUserClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public List<SearchUserRoot.SearchItem> getList() {
        return userlist;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public void addData(List<SearchUserRoot.SearchItem> user) {
        this.userlist.addAll(user);
        notifyItemRangeInserted(this.userlist.size(), user.size());
    }

    public interface OnItemUserClick {
        void onClick(SearchUserRoot.SearchItem userList);
    }

    public void clear() {
        userlist.clear();
        notifyDataSetChanged();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        ItemSearchUserBinding binding;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSearchUserBinding.bind(itemView);
        }

        public void setData(int position) {
            SearchUserRoot.SearchItem user = userlist.get(position);
            Glide.with(binding.getRoot()).load(user.getProfileImage()).into(binding.thumbnail);
            binding.username.setText(user.getName());
            binding.email.setText(user.getEmail());

            itemView.setOnClickListener(v -> {
                onItemClick.onClick(user);
            });
        }


    }
}
