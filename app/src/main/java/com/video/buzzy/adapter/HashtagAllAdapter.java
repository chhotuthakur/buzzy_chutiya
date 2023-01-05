package com.video.buzzy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemHashtagRecyclerBinding;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;

import java.util.ArrayList;
import java.util.List;

public class HashtagAllAdapter extends RecyclerView.Adapter<HashtagAllAdapter.HashtagHolder> {
    Context context;
    HashAdapter hashtagAdapter;
    OnViewAllClick onViewAllClick;
    List<HashtagVideoRoot.HashtagItem> hashtagItems = new ArrayList<>();

    public OnViewAllClick getOnViewAllClick() {
        return onViewAllClick;
    }

    public void setOnViewAllClick(OnViewAllClick onViewAllClick) {
        this.onViewAllClick = onViewAllClick;
    }


    @NonNull
    @Override
    public HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HashtagAllAdapter.HashtagHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hashtag_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagHolder holder, int position) {
        HashtagVideoRoot.HashtagItem hashtagItem = hashtagItems.get(position);

        hashtagAdapter = new HashAdapter(context);
        holder.binding.rvHashtagWiseVideo.setAdapter(hashtagAdapter);

        if (!hashtagItem.getReel().isEmpty()) {
            if (position % 2 == 0)
                holder.binding.hashtagBg.setImageResource(R.drawable.skill_shape_pink_10r);
            else holder.binding.hashtagBg.setImageResource(R.drawable.skill_shape_purple);

            Glide.with(context).load(BuildConfig.BASE_URL + hashtagItem.getImage()).placeholder(R.drawable.cloth1).into(holder.binding.hashtagImg);
            holder.binding.txthashtag.setText(hashtagItem.getHashtag());
            hashtagAdapter.addData(hashtagItem.getReel());

            // Glide.with(context).load(hashtagItem.getCoverImage()).into(holder.binding.hashtagBg);

        } else {
            holder.binding.hashtagVideoes.setVisibility(View.GONE);
        }


        hashtagAdapter.setOnHashtagClick(new HashAdapter.OnHashtagClick() {
            @Override
            public void onclick(int pos) {
                Log.d("reelsofhashtag", "onBindViewHolder: " + pos);
                onViewAllClick.onVideoClick(hashtagItems.get(position).getReel(), pos);
            }
        });

        holder.binding.viewAll.setOnClickListener(v -> {
            onViewAllClick.onClick(holder.binding.txthashtag.getText().toString());
        });


    }

    @Override
    public int getItemCount() {
        return hashtagItems.size();
    }

    public void addData(List<HashtagVideoRoot.HashtagItem> hashtagList) {
        hashtagItems.addAll(hashtagList);
        notifyItemRangeInserted(hashtagList.size(), hashtagList.size());
    }

    public interface OnViewAllClick {
        void onClick(String hashtag);

        void onVideoClick(List<HashtagVideoRoot.ReelItem> reelItemList, int pos);
    }
//

    public class HashtagHolder extends RecyclerView.ViewHolder {
        ItemHashtagRecyclerBinding binding;

        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHashtagRecyclerBinding.bind(itemView);
        }
    }
}
