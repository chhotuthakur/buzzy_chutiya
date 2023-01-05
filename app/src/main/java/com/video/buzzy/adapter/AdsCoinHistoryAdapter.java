package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemAdsHistoryBinding;
import com.video.buzzy.modelRetrofit.CoinHistoryRoot;

import java.util.ArrayList;
import java.util.List;

public class AdsCoinHistoryAdapter extends RecyclerView.Adapter<AdsCoinHistoryAdapter.HistoryHolder> {
    List<CoinHistoryRoot.HistoryItem> historyItemList = new ArrayList<>();
    Context context;
    String historyText;
    private String pending;

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }

    public void addData(List<CoinHistoryRoot.HistoryItem> redeemItems) {
        historyItemList.addAll(redeemItems);
        notifyItemRangeInserted(historyItemList.size(), redeemItems.size());
    }

    public void setTextHistory(String pending) {
        this.pending = pending;
    }

    public void clear() {
        historyItemList.clear();
        notifyDataSetChanged();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        ItemAdsHistoryBinding binding;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAdsHistoryBinding.bind(itemView);
        }

        public void setData(int position) {
            CoinHistoryRoot.HistoryItem historyItem = historyItemList.get(position);

            String date = historyItem.getDate();
            String[] separated = date.split(",");

            binding.tvcoin.setText(historyItem.getCoin() + " " + "Coin Get From Ads.");
            binding.tvdate.setText(separated[0]);

        }
    }
}
