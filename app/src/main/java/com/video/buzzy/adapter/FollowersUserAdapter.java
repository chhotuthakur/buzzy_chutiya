package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemFollowersListBinding;
import com.video.buzzy.modelRetrofit.UserFollowersRoot;

import java.util.ArrayList;
import java.util.List;

public class FollowersUserAdapter extends RecyclerView.Adapter<FollowersUserAdapter.FollowersHolder> {
    List<UserFollowersRoot.UserItem> userlist = new ArrayList<>();
    int pagepostion;
    Context context;
    OnClickFollow onClickFollow;
    boolean isfollow;

    public FollowersUserAdapter() {
    }

    public OnClickFollow getOnClickFollow() {
        return onClickFollow;
    }

    public void setOnClickFollow(OnClickFollow onClickFollow) {
        this.onClickFollow = onClickFollow;
    }

    @NonNull
    @Override
    public FollowersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new FollowersHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followers_list, parent, false));
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

    public interface OnClickFollow {
        void onClick(UserFollowersRoot.UserItem userItem, int pos, ItemFollowersListBinding binding);
    }

    public class FollowersHolder extends RecyclerView.ViewHolder {
        ItemFollowersListBinding binding;

        public FollowersHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFollowersListBinding.bind(itemView);
        }

        public void setData(int position) {
            UserFollowersRoot.UserItem user = userlist.get(position);

                Glide.with(binding.getRoot()).load(user.getImage()).into(binding.thumbnail);

            binding.name.setText(user.getName());
            binding.username.setText(user.getUsername());

            if (user.isIsFollow()) {
                binding.btnFollow.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_following));
                binding.btnFollow.setText(R.string.following);
            } else {
                binding.btnFollow.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_lightblue));
                binding.btnFollow.setText(R.string.follow);
            }

            binding.btnFollow.setOnClickListener(v -> {
                onClickFollow.onClick(user, position, binding);
            });


        }
    }
}
