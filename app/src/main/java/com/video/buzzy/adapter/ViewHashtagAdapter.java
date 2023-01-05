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
import com.video.buzzy.databinding.ItemViewHashtagBinding;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;

import java.util.ArrayList;
import java.util.List;

public class ViewHashtagAdapter extends RecyclerView.Adapter<ViewHashtagAdapter.HashtagHolder> {
    Context context;
    List<HashtagVideoRoot.ReelItem> hashtag = new ArrayList<>();

    OnHashtagVideoClick onHashtagVideoClick;

    public OnHashtagVideoClick getOnHashtagVideoClick() {
        return onHashtagVideoClick;
    }

    public void setOnHashtagVideoClick(OnHashtagVideoClick onHashtagVideoClick) {
        this.onHashtagVideoClick = onHashtagVideoClick;
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagHolder holder, int position) {
        HashtagVideoRoot.ReelItem reelItem = hashtag.get(position);

        Glide.with(context).asBitmap().load(BuildConfig.BASE_URL + reelItem.getThumbnail()).into(holder.binding.videoThumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHashtagVideoClick.onclick(hashtag, position);
            }
        });


    }


    @NonNull
    @Override
    public HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHashtagAdapter.HashtagHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_hashtag, parent, false));
    }

    public interface OnHashtagVideoClick {
        void onclick(List<HashtagVideoRoot.ReelItem> reelItem, int pos);
    }

    @Override
    public int getItemCount() {
        return hashtag.size();
    }

    public void addData(List<HashtagVideoRoot.ReelItem> reelItems) {
        hashtag.addAll(reelItems);
        notifyItemRangeInserted(hashtag.size(), reelItems.size());
    }


    public class HashtagHolder extends RecyclerView.ViewHolder {
        ItemViewHashtagBinding binding;

        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemViewHashtagBinding.bind(itemView);
        }
    }
}
