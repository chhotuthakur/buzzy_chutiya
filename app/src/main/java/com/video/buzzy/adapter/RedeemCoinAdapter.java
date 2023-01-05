package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemRedeemCoinBinding;
import com.video.buzzy.modelRetrofit.RedeemPlanRoot;

import java.util.ArrayList;
import java.util.List;

public class RedeemCoinAdapter extends RecyclerView.Adapter<RedeemCoinAdapter.CoinHolder> {
    Context context;
    List<RedeemPlanRoot.RedeemPlanItem> redeemPlanList = new ArrayList<>();

    OnClickReedem onClickReedem;

    public OnClickReedem getOnClickReedem() {
        return onClickReedem;
    }

    public void setOnClickReedem(OnClickReedem onClickReedem) {
        this.onClickReedem = onClickReedem;
    }

    @Override
    public int getItemCount() {
        return redeemPlanList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CoinHolder holder, int position) {
        holder.setData(position);
    }


    @NonNull
    @Override
    public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CoinHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_redeem_coin, parent, false));
    }

    public void addData(List<RedeemPlanRoot.RedeemPlanItem> redeemPlanItems) {
        redeemPlanList.addAll(redeemPlanItems);
        notifyItemRangeInserted(redeemPlanList.size(), redeemPlanItems.size());
    }

    public interface OnHashtagClick {
        void onclick();
    }

    public interface OnClickReedem {
        void onClick(RedeemPlanRoot.RedeemPlanItem redeemPlanItem);
    }

    public class CoinHolder extends RecyclerView.ViewHolder {
        ItemRedeemCoinBinding binding;

        public CoinHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRedeemCoinBinding.bind(itemView);
        }

        public void setData(int position) {
            RedeemPlanRoot.RedeemPlanItem redeemPlanItem = redeemPlanList.get(position);

            binding.txtTotal.setText(String.valueOf(redeemPlanItem.getDiamond()));
            binding.rupee.setText(redeemPlanItem.getRupee() + "₹");

            binding.rupee.setOnClickListener(v -> {
                onClickReedem.onClick(redeemPlanItem);

            });

        }
    }
}
