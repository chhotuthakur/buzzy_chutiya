package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemGiftNotificationBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;

import java.util.ArrayList;
import java.util.List;

public class GiftNotificationAdapter extends RecyclerView.Adapter<GiftNotificationAdapter.GiftViewHolder> {

    Context context;
    private List<NotificationRoot.NotificationsItem> gifts = new ArrayList<>();


    @Override
    public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new GiftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift_notification, parent, false));
    }


    @Override
    public void onBindViewHolder(GiftViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public List<NotificationRoot.NotificationsItem> getList() {
        return gifts;
    }

    public void addData(List<NotificationRoot.NotificationsItem> gift) {
        this.gifts.addAll(gift);
        notifyItemRangeInserted(this.gifts.size(), gift.size());
    }


    public class GiftViewHolder extends RecyclerView.ViewHolder {
        ItemGiftNotificationBinding binding;

        public GiftViewHolder(View itemView) {
            super(itemView);
            binding = ItemGiftNotificationBinding.bind(itemView);
        }

        public void setData(int position) {

            NotificationRoot.NotificationsItem gift = gifts.get(position);
            Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + gift.getImage()).into(binding.userImg);


            binding.comment.setText(gift.getMessage());


        }
    }

}
