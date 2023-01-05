package com.video.buzzy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemConvertCoinBinding;
import com.video.buzzy.modelRetrofit.DiamondPlanRoot;

import java.util.ArrayList;
import java.util.List;

public class ConvertCoinAdapter extends RecyclerView.Adapter<ConvertCoinAdapter.ConvertCoinHolder> {
    Context context;
    List<DiamondPlanRoot.DiamondPlanItem> diamondPlanlist = new ArrayList<>();

    OnClickConvert onClickConvert;

    public OnClickConvert getOnClickConvert() {
        return onClickConvert;
    }

    public void setOnClickConvert(OnClickConvert onClickConvert) {
        this.onClickConvert = onClickConvert;
    }

    public void clear() {
        diamondPlanlist.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ConvertCoinHolder holder, int position) {
        holder.setData(position);
    }

    @NonNull
    @Override
    public ConvertCoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ConvertCoinHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_convert_coin, parent, false));
    }

    @Override
    public int getItemCount() {
        return diamondPlanlist.size();
    }

    public void addData(List<DiamondPlanRoot.DiamondPlanItem> diamondPlanItems) {
        diamondPlanlist.addAll(diamondPlanItems);
        notifyItemRangeInserted(diamondPlanlist.size(), diamondPlanItems.size());
    }

    public interface OnClickConvert {
        void onClick(DiamondPlanRoot.DiamondPlanItem diamondPlanItem, ItemConvertCoinBinding binding);
    }


    public class ConvertCoinHolder extends RecyclerView.ViewHolder {
        ItemConvertCoinBinding binding;

        public ConvertCoinHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemConvertCoinBinding.bind(itemView);
        }

        public void setData(int position) {

            DiamondPlanRoot.DiamondPlanItem diamondPlanItem = diamondPlanlist.get(position);

            binding.txtTotal.setText(String.valueOf(diamondPlanItem.getDiamond()));
            binding.coin.setText(diamondPlanItem.getCoin() + " " + "Coins");

            binding.coin.setOnClickListener(v -> {
                onClickConvert.onClick(diamondPlanItem, binding);
            });

        }
    }
}
