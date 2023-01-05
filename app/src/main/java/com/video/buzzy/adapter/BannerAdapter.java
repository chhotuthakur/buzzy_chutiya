package com.video.buzzy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.activity.WebActivity;
import com.video.buzzy.databinding.ItemBannerBinding;
import com.video.buzzy.modelRetrofit.BannerRoot;
import com.video.buzzy.util.Const;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {
    Context context;
    List<BannerRoot.BannerItem> bannerItemList = new ArrayList<>();


    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BannerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
        BannerRoot.BannerItem bannerItem = bannerItemList.get(position);
        Glide.with(context).load(BuildConfig.BASE_URL + bannerItem.getImage()).into(holder.binding.bannerImage);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, WebActivity.class);
            i.putExtra(Const.URL, bannerItem.getURL());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return bannerItemList.size();
    }

    public void addData(List<BannerRoot.BannerItem> banner) {
        bannerItemList.addAll(banner);
        notifyItemRangeInserted(bannerItemList.size(), banner.size());
    }

    public class BannerHolder extends RecyclerView.ViewHolder {
        ItemBannerBinding binding;

        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemBannerBinding.bind(itemView);
        }
    }
}
