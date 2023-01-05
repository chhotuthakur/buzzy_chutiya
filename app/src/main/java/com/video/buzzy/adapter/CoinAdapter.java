package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemTotalCoinBinding;
import com.video.buzzy.modelRetrofit.CoinPlanRoot;
import com.video.buzzy.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinHolder> {
    Context context;
    OnClickItem onClickItem;
    SessionManager sessionManager;
    int adCount = 0;

    List<CoinPlanRoot.CoinPlanItem> coinPlanItems = new ArrayList<>();


    public OnClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    @Override
    public int getItemCount() {
        return coinPlanItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CoinHolder holder, int position) {
        holder.setData(position);
    }


    @NonNull
    @Override
    public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManager = new SessionManager(context);
        return new CoinHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_total_coin, parent, false));
    }

    public void addData(List<CoinPlanRoot.CoinPlanItem> coinPlanItem) {
        coinPlanItems.addAll(coinPlanItem);
        notifyItemRangeInserted(coinPlanItems.size(), coinPlanItems.size());
    }


    public interface OnClickItem {
        void onClick(CoinPlanRoot.CoinPlanItem coinPlanItem);

        void onAdsClick(CoinPlanRoot.CoinPlanItem coinPlanItem, ItemTotalCoinBinding binding);
    }

    public class CoinHolder extends RecyclerView.ViewHolder {
        ItemTotalCoinBinding binding;

        public CoinHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTotalCoinBinding.bind(itemView);
        }

        public void setData(int position) {

            if (position == 0) {
                binding.adsLay.setVisibility(View.VISIBLE);
                binding.price.setVisibility(View.GONE);
            } else {
                binding.adsLay.setVisibility(View.GONE);
                binding.price.setVisibility(View.VISIBLE);
            }

            CoinPlanRoot.CoinPlanItem coinPlanItem = coinPlanItems.get(position);

            binding.txtTotal.setText(String.valueOf(coinPlanItem.getCoin()));
            binding.price.setText(context.getString(R.string.rupee) + " " + coinPlanItem.getRupee());

            binding.adsLay.setOnClickListener(v -> {
                adCount = adCount + 1;
                if (adCount > sessionManager.getSetting().getMaxAdPerDay()) {
                    Toast.makeText(context, "Your ads limit was over!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                onClickItem.onAdsClick(coinPlanItem, binding);
            });


            binding.getRoot().setOnClickListener(v -> {
                if (position != 0) {
                    onClickItem.onClick(coinPlanItem);
                }
            });
        }
    }
}
