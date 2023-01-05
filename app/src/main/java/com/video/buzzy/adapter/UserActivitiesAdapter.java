package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemActivitiesBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;

import java.util.ArrayList;
import java.util.List;

public class UserActivitiesAdapter extends RecyclerView.Adapter<UserActivitiesAdapter.HashtagHolder> {
    Context context;
    List<NotificationRoot.NotificationsItem> notificationsItems = new ArrayList<>();
    int selected_pos = -1;
    OnClickDelete onClickDelete;

    public OnClickDelete getOnClickDelete() {
        return onClickDelete;
    }

    public void setOnClickDelete(OnClickDelete onClickDelete) {
        this.onClickDelete = onClickDelete;
    }

    @NonNull
    @Override
    public HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new UserActivitiesAdapter.HashtagHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagHolder holder, int position) {
        holder.setData(position);

    }

    @Override
    public int getItemCount() {
        return notificationsItems.size();
    }

    public void addData(List<NotificationRoot.NotificationsItem> hashtagList) {
        notificationsItems.addAll(hashtagList);
        notifyItemRangeInserted(notificationsItems.size(), hashtagList.size());
    }

    public void clear() {
        notificationsItems.clear();
        notifyDataSetChanged();
    }

    public void removeItem(NotificationRoot.NotificationsItem notificationsItem) {
        notificationsItems.remove(notificationsItem);
        notifyDataSetChanged();
    }

    public interface OnClickDelete {
        void onClick(NotificationRoot.NotificationsItem notificationsItem);
    }

    public class HashtagHolder extends RecyclerView.ViewHolder {
        ItemActivitiesBinding binding;

        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemActivitiesBinding.bind(itemView);
        }

        public void setData(int position) {

            if (selected_pos != position) {
                if (binding.swipeLayout.isRightOpen()) {
                    binding.swipeLayout.close();
                }
            }

            NotificationRoot.NotificationsItem notificationsItem = notificationsItems.get(position);

            Glide.with(context).load(notificationsItem.getImage()).into(binding.userImg);
            binding.comment.setText(notificationsItem.getMessage().trim());
            binding.username.setText(notificationsItem.getName());
            binding.date.setText(notificationsItem.getTime());


            binding.ivDelete.setOnClickListener(v -> {
               // Toast.makeText(context, "clicked " + position, Toast.LENGTH_SHORT).show();
                onClickDelete.onClick(notificationsItem);

                selected_pos = position;
                notifyDataSetChanged();

            });
        }
    }
}
