package com.video.buzzy.util.autocomplete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;
import com.video.buzzy.util.TextFormatUtil;

import java.util.ArrayList;
import java.util.List;


public class HashtagAdapter extends RecyclerView.Adapter<HashtagAdapter.HashtagViewHolder> {

    private final Context mContext;
    private final OnClickListener mListener;
    private List<HashtagVideoRoot.HashtagItem> mItems = new ArrayList<>();

    protected HashtagAdapter(@NonNull Context context, @NonNull OnClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagViewHolder holder, int position) {
        HashtagVideoRoot.HashtagItem hashtag = mItems.get(position);
        holder.name.setText("#" + hashtag.getHashtag());
        holder.clips.setText(
                mContext.getString(R.string.count_clips, TextFormatUtil.toShortNumber(hashtag.getVideoCount())));
        holder.itemView.setOnClickListener(v -> mListener.onHashtagClick(hashtag));
    }

    @NonNull
    @Override
    public HashtagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext)
                .inflate(R.layout.item_hashtag_slim, parent, false);
        return new HashtagViewHolder(root);
    }

    public void submitData(List<HashtagVideoRoot.HashtagItem> items) {
        mItems.addAll(items);
        notifyItemRangeInserted(mItems.size(), items.size());
    }

    interface OnClickListener {

        void onHashtagClick(HashtagVideoRoot.HashtagItem hashtag);
    }

    public class HashtagViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView clips;

        public HashtagViewHolder(@NonNull View root) {
            super(root);
            name = root.findViewById(R.id.name);
            clips = root.findViewById(R.id.clips);
        }
    }
}
