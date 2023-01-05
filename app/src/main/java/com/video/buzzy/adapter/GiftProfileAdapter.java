package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemProfilegiftBinding;
import com.video.buzzy.design.Gift;
import com.video.buzzy.modelRetrofit.GiftRoot;

import java.util.ArrayList;
import java.util.List;

public class GiftProfileAdapter extends RecyclerView.Adapter<GiftProfileAdapter.GiftViewHolder> {

    Context context;
    boolean isshowcount = false;
    OnGiftClick onGiftClick;
    private List<GiftRoot.GiftItem> gifts = new ArrayList<>();

    public OnGiftClick getOnGiftClick() {
        return onGiftClick;
    }

    public void setOnGiftClick(OnGiftClick onGiftClick) {
        this.onGiftClick = onGiftClick;
    }

    public void isshow(boolean isshowcount) {
        this.isshowcount = isshowcount;
    }

    @Override
    public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new GiftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item__profilegift, parent, false));
    }


    @Override
    public void onBindViewHolder(GiftViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public List<GiftRoot.GiftItem> getList() {
        return gifts;
    }

    public void addData(List<GiftRoot.GiftItem> gift) {
        this.gifts.addAll(gift);
        notifyItemRangeInserted(this.gifts.size(), gift.size());
    }

    public void clear() {
        gifts.clear();
        notifyDataSetChanged();
    }

    public interface OnGiftClick {
        void onGiftClick(int position, Gift gift);
    }

    public class GiftViewHolder extends RecyclerView.ViewHolder {
        ItemProfilegiftBinding binding;

        public GiftViewHolder(View itemView) {
            super(itemView);
            binding = ItemProfilegiftBinding.bind(itemView);
        }

        public void setData(int position) {

            GiftRoot.GiftItem gift = gifts.get(position);
            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + gift.getImage()).into(binding.giftImg);

            if (gift.getReceivedcount() == 0) {
                binding.senderCount.setVisibility(View.GONE);
                binding.giftImg.setImageTintList(ContextCompat.getColorStateList(context, R.color.gift_tint));
            } else {
                binding.giftImg.setImageTintList(null);
                binding.senderCount.setText("X" + gift.getReceivedcount());
            }


//            if (gift.getTotalsender() > 0) {
//                binding.senderCount.setVisibility(View.VISIBLE);
//                binding.senderCount.setText("X" + gift.getTotalsender());
//            } else {
//                binding.senderCount.setVisibility(View.GONE);
//            }

//            itemView.setOnClickListener(v -> {
//
//                try {
//                    onGiftClick.onGiftClick(position, gift);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            });


        }
    }

}
