package com.video.buzzy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemPostVideoBinding;
import com.video.buzzy.modelRetrofit.AllVideoRoot;

import java.util.ArrayList;
import java.util.List;

public class UserPostVideoAdapter extends RecyclerView.Adapter<UserPostVideoAdapter.PostVideoViewHolder> {

    private List<AllVideoRoot.ReelItem> userPost = new ArrayList<>();

    OnItemUserVideoClick onItemUserVideoClick;

    public OnItemUserVideoClick getOnItemUserVideoClick() {
        return onItemUserVideoClick;
    }

    public void setOnItemUserVideoClick(OnItemUserVideoClick onItemUserVideoClick) {
        this.onItemUserVideoClick = onItemUserVideoClick;
    }

    public void clear() {
        userPost.clear();
        notifyDataSetChanged();
    }

    public List<AllVideoRoot.ReelItem> getList() {
        return userPost;
    }


    @Override
    public PostVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostVideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_video, parent, false));
    }


    @Override
    public void onBindViewHolder(PostVideoViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return userPost.size();
    }

    public void addData(List<AllVideoRoot.ReelItem> userpost) {
        this.userPost.addAll(userpost);
        notifyItemRangeInserted(this.userPost.size(), userpost.size());
    }

    public interface OnItemUserVideoClick {
        void onClick(int pos, List<AllVideoRoot.ReelItem> reelItem);
    }

    public class PostVideoViewHolder extends RecyclerView.ViewHolder {
        ItemPostVideoBinding binding;

        public PostVideoViewHolder(View itemView) {
            super(itemView);
            binding = ItemPostVideoBinding.bind(itemView);
        }

        public void setData(int position) {
            AllVideoRoot.ReelItem post = userPost.get(position);

            Glide.with(binding.getRoot()).asBitmap().load(BuildConfig.BASE_URL + post.getThumbnail()).into(binding.postThumbnail);

            if (post.getCaption() == null) {
                binding.bio.setVisibility(View.GONE);
            } else {
                binding.bio.setVisibility(View.VISIBLE);
                binding.bio.setText(post.getCaption());
            }

            itemView.setOnClickListener(v -> {
                onItemUserVideoClick.onClick(position, userPost);
            });

        }
    }

}
