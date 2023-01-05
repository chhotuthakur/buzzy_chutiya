package com.video.buzzy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemSearchHashtagBinding;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;

import java.util.ArrayList;
import java.util.List;

public class HashtagListAdapter extends RecyclerView.Adapter<HashtagListAdapter.HashtagHolder> {
    List<HashtagVideoRoot.HashtagItem> hashtaglist = new ArrayList<>();
    OnItemClick onItemClick;

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HashtagHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_hashtag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return hashtaglist.size();
    }

    public List<HashtagVideoRoot.HashtagItem> getList() {
        return hashtaglist;
    }

    public void addData(List<HashtagVideoRoot.HashtagItem> user) {
        this.hashtaglist.addAll(user);
        notifyItemRangeInserted(this.hashtaglist.size(), user.size());
    }

    public interface OnItemClick {
        void onClick(HashtagVideoRoot.HashtagItem hashtagItem);
    }

    public void clear() {
        hashtaglist.clear();
        notifyDataSetChanged();
    }


    public class HashtagHolder extends RecyclerView.ViewHolder {
        ItemSearchHashtagBinding binding;

        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSearchHashtagBinding.bind(itemView);
        }

        public void setData(int position) {
            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + hashtaglist.get(position).getImage()).into(binding.thumbnail);
            binding.hashtagname.setText(hashtaglist.get(position).getHashtag());

            itemView.setOnClickListener(v -> {
                onItemClick.onClick(hashtaglist.get(position));
            });
        }

    }
}
