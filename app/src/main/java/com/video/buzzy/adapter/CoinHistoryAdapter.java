package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemCoinHistoryBinding;
import com.video.buzzy.modelRetrofit.CoinHistoryRoot;

import java.util.ArrayList;
import java.util.List;

public class CoinHistoryAdapter extends RecyclerView.Adapter<CoinHistoryAdapter.HistoryHolder> {
    List<CoinHistoryRoot.HistoryItem> historyItemList = new ArrayList<>();
    Context context;
    String historyText;
    private String pending;

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coin_history, parent, false));
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
        ItemCoinHistoryBinding binding;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCoinHistoryBinding.bind(itemView);
        }

        public void setData(int position) {
            CoinHistoryRoot.HistoryItem historyItem = historyItemList.get(position);

            String date = historyItem.getDate();
            String[] separated = date.split(",");

            binding.tvDiamond.setText(historyItem.getDiamond() + " " + "Diamonds");
            binding.tvcoin.setText("Convert into" + " " + historyItem.getCoin() + " " + "Coin");
            binding.tvdate.setText(separated[0]);

        }
    }
}
