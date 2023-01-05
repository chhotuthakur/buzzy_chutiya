package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemCommentlistBinding;
import com.video.buzzy.modelRetrofit.NotificationRoot;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    List<NotificationRoot.NotificationsItem> comments = new ArrayList<>();
    Context context;


    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.setData(position);
    }

    public void addSingleMessage(NotificationRoot.NotificationsItem dataItem) {
        comments.add(dataItem);
        notifyItemInserted(comments.size() - 1);
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addData(List<NotificationRoot.NotificationsItem> comment) {
        comments.addAll(comment);
        notifyItemRangeInserted(comments.size(), comment.size());
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        ItemCommentlistBinding binding;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommentlistBinding.bind(itemView);
        }


        public void setData(int position) {
            NotificationRoot.NotificationsItem comment = comments.get(position);

            Glide.with(context).load(comment.getImage()).into(binding.userImg);
            binding.comment.setText(comment.getMessage());
            binding.date.setText(comment.getTime());
            binding.username.setText(comment.getName() + "Commented On Your Post.");

        }
    }
}
