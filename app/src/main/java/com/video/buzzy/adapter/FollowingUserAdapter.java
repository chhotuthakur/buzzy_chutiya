package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemFollowingListBinding;
import com.video.buzzy.modelRetrofit.UserFollowersRoot;

import java.util.ArrayList;
import java.util.List;

public class FollowingUserAdapter extends RecyclerView.Adapter<FollowingUserAdapter.FollowersHolder> {
    List<UserFollowersRoot.UserItem> userlist = new ArrayList<>();
    int pagepostion;
    Context context;
    OnClickFollowing onClickFollowing;

    public FollowingUserAdapter() {
    }

    public OnClickFollowing getOnClickFollowing() {
        return onClickFollowing;
    }

    public void setOnClickFollowing(OnClickFollowing onClickFollowing) {
        this.onClickFollowing = onClickFollowing;
    }

    @NonNull
    @Override
    public FollowersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new FollowersHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public List<UserFollowersRoot.UserItem> getList() {
        return userlist;
    }

    public void addData(List<UserFollowersRoot.UserItem> user) {
        this.userlist.addAll(user);
        notifyItemRangeInserted(this.userlist.size(), user.size());
    }


    public void clear() {
        userlist.clear();
        notifyDataSetChanged();
    }

    public interface OnClickFollowing {
        void onClick(UserFollowersRoot.UserItem userItem, int pos, ItemFollowingListBinding binding);
    }

    public class FollowersHolder extends RecyclerView.ViewHolder {
        ItemFollowingListBinding binding;

        public FollowersHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFollowingListBinding.bind(itemView);
        }

        public void setData(int position) {
            UserFollowersRoot.UserItem user = userlist.get(position);


            Glide.with(binding.getRoot()).load(user.getImage()).into(binding.thumbnail);

            binding.name.setText(user.getName());
            binding.username.setText(user.getUsername());


            binding.btnFollow.setOnClickListener(v -> {
                onClickFollowing.onClick(user, position, binding);

            });


        }
    }
}
