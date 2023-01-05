package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemMentionBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;

import java.util.ArrayList;
import java.util.List;

public class MentionAdapter extends RecyclerView.Adapter<MentionAdapter.MentionHolder> {
    List<NotificationRoot.NotificationsItem> mentions = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MentionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MentionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mention, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MentionHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mentions.size();
    }

    public void addData(List<NotificationRoot.NotificationsItem> mention) {
        mentions.addAll(mention);
        notifyItemRangeInserted(mentions.size(), mention.size());
    }

    public class MentionHolder extends RecyclerView.ViewHolder {
        ItemMentionBinding binding;

        public MentionHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMentionBinding.bind(itemView);
        }

        public void setData(int position) {
            NotificationRoot.NotificationsItem mention = mentions.get(position);

            Glide.with(context).load(mention.getImage()).into(binding.userImg);
            binding.username.setText(mention.getName());
            binding.comment.setText(mention.getMessage());
            binding.date.setText(mention.getTime());

        }
    }
}
