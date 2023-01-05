package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemGiftBinding;
import com.video.buzzy.modelRetrofit.GiftRoot;

import java.util.ArrayList;
import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    Context context;
    private List<GiftRoot.GiftItem> gifts = new ArrayList<>();
    boolean isshowcount = false;
    OnGiftClick onGiftClick;
    int selectpos = -1;

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
        return new GiftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent, false));
    }

    public void refreshSelectValue() {
        selectpos = -1;
    }

    @Override
    public void onBindViewHolder(GiftViewHolder holder, int position) {

        if (selectpos == position)
            holder.binding.selectedLay.setVisibility(View.VISIBLE);
        else
            holder.binding.selectedLay.setVisibility(View.GONE);

        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public void clear() {
        gifts.clear();
        notifyDataSetChanged();
    }

//    public void refreshSelectValue() {
//        selectpos = -1;
//    }

    public List<GiftRoot.GiftItem> getList() {
        return gifts;
    }

    public void addData(List<GiftRoot.GiftItem> gift) {
        this.gifts.addAll(gift);
        notifyItemRangeInserted(this.gifts.size(), gift.size());
    }

    public interface OnGiftClick {
        void onGiftClick(GiftRoot.GiftItem gift);
        void OnSendGiftClick(GiftRoot.GiftItem gift);
    }

    public class GiftViewHolder extends RecyclerView.ViewHolder {
        ItemGiftBinding binding;

        public GiftViewHolder(View itemView) {
            super(itemView);
            binding = ItemGiftBinding.bind(itemView);
        }

        public void setData(int position) {
            GiftRoot.GiftItem gift = gifts.get(position);
            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + gift.getImage()).placeholder(R.drawable.gift).into(binding.giftImg);
            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + gift.getImage()).placeholder(R.drawable.gift).into(binding.selectGift);

            binding.selectedCoin.setText(gift.getCoin() + " " + "Coins");

            binding.btnSend.setOnClickListener(v -> {
                onGiftClick.OnSendGiftClick(gift);
            });

            // binding.name.setText(gift.getName());
            binding.coins.setText(gift.getCoin() + " " + "Coins");

            itemView.setOnClickListener(v -> {
                onGiftClick.onGiftClick(gift);
                selectpos = position;
                notifyDataSetChanged();
            });


        }
    }

}
