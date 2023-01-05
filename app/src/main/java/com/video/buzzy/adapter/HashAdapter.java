package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemHashtagBinding;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;

import java.util.ArrayList;
import java.util.List;

public class HashAdapter extends RecyclerView.Adapter<HashAdapter.HashtagHolder> {
    Context context;
    List<HashtagVideoRoot.ReelItem> hashtag = new ArrayList<>();

    OnHashtagClick onHashtagClick;

    public OnHashtagClick getOnHashtagClick() {
        return onHashtagClick;
    }

    public void setOnHashtagClick(OnHashtagClick onHashtagClick) {
        this.onHashtagClick = onHashtagClick;
    }

    public HashAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagHolder holder, int position) {
        HashtagVideoRoot.ReelItem reelItem = hashtag.get(position);

        Glide.with(context).asBitmap().load(BuildConfig.BASE_URL + reelItem.getThumbnail()).into(holder.binding.videoThumb);

        holder.itemView.setOnClickListener(v -> {
            onHashtagClick.onclick(position);
        });
    }


    @NonNull
    @Override
    public HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HashAdapter.HashtagHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hashtag, parent, false));
    }

    public interface OnHashtagClick {
        void onclick(int pos);
    }

    @Override
    public int getItemCount() {
        return hashtag.size();
    }

    public void addData(List<HashtagVideoRoot.ReelItem> hashtagList) {
        hashtag.addAll(hashtagList);
        notifyItemRangeInserted(hashtag.size(), hashtagList.size());
    }


    public class HashtagHolder extends RecyclerView.ViewHolder {
        ItemHashtagBinding binding;

        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHashtagBinding.bind(itemView);
        }
    }
}
