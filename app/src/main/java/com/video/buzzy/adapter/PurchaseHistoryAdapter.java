package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemPurchaseHistoryBinding;
import com.video.buzzy.modelRetrofit.CoinHistoryRoot;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.HistoryHolder> {
    List<CoinHistoryRoot.HistoryItem> historyItemList = new ArrayList<>();
    Context context;
    String historyText;
    private String pending;

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_history, parent, false));
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
        ItemPurchaseHistoryBinding binding;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPurchaseHistoryBinding.bind(itemView);
        }

        public void setData(int position) {
            CoinHistoryRoot.HistoryItem historyItem = historyItemList.get(position);

            String date = historyItem.getDate();
            String[] separated = date.split(",");

            binding.tvcoin.setText(historyItem.getCoin() + " " + "Coin Purchased");
            binding.tvPaymentGateway.setText(historyItem.getPaymentGateway());
            binding.tvdate.setText(separated[0]);

        }
    }
}
