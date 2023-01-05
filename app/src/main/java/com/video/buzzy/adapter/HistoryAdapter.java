package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemHistoryBinding;
import com.video.buzzy.modelRetrofit.RedeeHistoryRoot;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    List<RedeeHistoryRoot.RedeemItem> redeemItemList = new ArrayList<>();
    Context context;
    String historyText;
    private String pending;

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return redeemItemList.size();
    }

    public void addData(List<RedeeHistoryRoot.RedeemItem> redeemItems) {
        redeemItemList.addAll(redeemItems);
        notifyItemRangeInserted(redeemItemList.size(), redeemItems.size());
    }

    public void setTextHistory(String pending) {
        this.pending = pending;
    }

    public void clear() {
        redeemItemList.clear();
        notifyDataSetChanged();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        ItemHistoryBinding binding;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHistoryBinding.bind(itemView);
        }

        public void setData(int position) {
            RedeeHistoryRoot.RedeemItem redeemItem = redeemItemList.get(position);

            String date = redeemItem.getDate();
            String[] separated = date.split(",");

            if (pending.equalsIgnoreCase("pending")) {
                binding.tvStatus.setText("Pending");
                binding.tvStatus.setTextColor(ContextCompat.getColorStateList(context, R.color.light_blue));
            } else if (pending.equalsIgnoreCase("accepted")) {
                binding.tvStatus.setText("Accepted");
                binding.tvStatus.setTextColor(ContextCompat.getColorStateList(context, R.color.online_color));
            } else if (pending.equalsIgnoreCase("decline")) {
                binding.tvStatus.setText("Decline");
                binding.tvStatus.setTextColor(ContextCompat.getColorStateList(context, R.color.red));
            }

            binding.tvcoin.setText(redeemItem.getDiamond() + " " + "Diamonds");
            binding.tvPaymentGateway.setText(redeemItem.getPaymentGateway());
            binding.tvdate.setText(separated[0]);

        }
    }
}
