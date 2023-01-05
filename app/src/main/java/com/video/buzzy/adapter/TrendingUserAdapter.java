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
import com.video.buzzy.databinding.ItemFollowingListBinding;
import com.video.buzzy.modelRetrofit.TrendingUserRoot;

import java.util.ArrayList;
import java.util.List;

public class TrendingUserAdapter extends RecyclerView.Adapter<TrendingUserAdapter.SuggestHolder> {
    List<TrendingUserRoot.UserItem> userlist = new ArrayList<>();
    int Pagepostion;
    Context context;

    OnClickUser onClickUser;

    public OnClickUser getOnClickUser() {
        return onClickUser;
    }

    public void setOnClickUser(OnClickUser onClickUser) {
        this.onClickUser = onClickUser;
    }


    public TrendingUserAdapter() {
    }

    @NonNull
    @Override
    public SuggestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new TrendingUserAdapter.SuggestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following_list, parent, false));
    }

    public List<TrendingUserRoot.UserItem> getList() {
        return userlist;
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public void addData(List<TrendingUserRoot.UserItem> user) {
        this.userlist.addAll(user);
        notifyItemRangeInserted(this.userlist.size(), user.size());
    }

    public void clear() {
        userlist.clear();
        notifyDataSetChanged();
    }


    public interface OnClickUser {
        void onClick(TrendingUserRoot.UserItem suggestedUser);

        void OnClickFollow(TrendingUserRoot.UserItem userItem, ItemFollowingListBinding binding);
    }

    public class SuggestHolder extends RecyclerView.ViewHolder {
        ItemFollowingListBinding binding;

        public SuggestHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFollowingListBinding.bind(itemView);
        }

        public void setData(int position) {
            TrendingUserRoot.UserItem user = userlist.get(position);
            Glide.with(binding.getRoot()).load(user.getProfileImage()).into(binding.thumbnail);

            binding.name.setText(user.getName());
            binding.username.setText(user.getUsername());

            binding.btnFollow.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_lightblue));
            binding.btnFollow.setText(R.string.follow);

            binding.btnFollow.setOnClickListener(v -> {
                onClickUser.OnClickFollow(user, binding);
            });

            binding.thumbnail.setOnClickListener(v -> {
                onClickUser.onClick(user);
            });


        }
    }
}
