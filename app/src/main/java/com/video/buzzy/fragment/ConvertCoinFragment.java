package com.video.buzzy.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.video.buzzy.R;
import com.video.buzzy.adapter.ConvertCoinAdapter;
import com.video.buzzy.databinding.FragmentCovertCoinBinding;
import com.video.buzzy.databinding.FragmentMyIncomeFragmentBinding;
import com.video.buzzy.databinding.ItemConvertCoinBinding;
import com.video.buzzy.modelRetrofit.DiamondPlanRoot;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.popupbuilder.PopupBuilder;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertCoinFragment extends Fragment {

    FragmentCovertCoinBinding binding;
    ConvertCoinAdapter coinAdapter;
    Dialog mBuilder;
    int myRcoin = 3000;
    SessionManager sessionManager;
    private PopupBuilder popupBuilder;
    private com.video.buzzy.databinding.FragmentMyIncomeFragmentBinding incomeBinding;
    private ItemConvertCoinBinding itemCovertbinding;


    public ConvertCoinFragment() {
    }

    public ConvertCoinFragment(FragmentMyIncomeFragmentBinding incomeBinding) {
        this.incomeBinding = incomeBinding;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_covert_coin, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {


        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        sessionManager = new SessionManager(getActivity());
        popupBuilder = new PopupBuilder(getActivity());


        mBuilder = new Dialog(getActivity());
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);

        if (mBuilder != null && mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        coinAdapter = new ConvertCoinAdapter();
        binding.rvConvertCoin.setAdapter(coinAdapter);


        getDiamondPlan();


        coinAdapter.setOnClickConvert((diamondPlanItem, binding) -> {

//            PopupBuilder popupBuilder = new PopupBuilder(getActivity());
//            popupBuilder.showRcoinConvertPopup(false, myRcoin, new OnRcoinConvertPopupClickListner() {
//                @Override
//                public void onClickConvert(int rcoin) {
//                    double dimonds = rcoin / 100;
//                    String s = "Your " + rcoin + " Rcoin Successfully Converted into " + dimonds + " Diamonds";
//                    popupBuilder.showSimplePopup(s, "Continue");
//                }
//            });

            if (sessionManager.getUser().getDiamond() >= diamondPlanItem.getDiamond()) {

                itemCovertbinding = binding;

                Dialog dialog = new Dialog(ConvertCoinFragment.this.getActivity());
                dialog.setContentView(R.layout.convert_alert_popup);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView diamond = dialog.findViewById(R.id.diamond);
                TextView coin = dialog.findViewById(R.id.coin);
                Button yes = dialog.findViewById(R.id.yes);
                Button no = dialog.findViewById(R.id.no);

                diamond.setText(diamondPlanItem.getDiamond() + " " + "Diamond");

                coin.setText(diamondPlanItem.getCoin() + " " + "Coin");

                yes.setOnClickListener(v -> {
                    if (sessionManager.getUser().getDiamond() > diamondPlanItem.getDiamond()) {
                        ConvertCoinFragment.this.converRcoinToDiamond(diamondPlanItem.getId(), diamondPlanItem.getCoin(), diamondPlanItem.getDiamond());
                    }
                    dialog.dismiss();
                });

                no.setOnClickListener(v -> {
                    dialog.dismiss();
                });

                dialog.show();

            } else {
                Toast.makeText(ConvertCoinFragment.this.getActivity(), "You haven't enough diamond for covert into coin", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void converRcoinToDiamond(String planId, int coin, int dimonds) {
        // binding.loder.setVisibility(View.VISIBLE);
        itemCovertbinding.loder.setVisibility(View.VISIBLE);
        itemCovertbinding.coin.setVisibility(View.INVISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", sessionManager.getUser().getId());
        jsonObject.addProperty("planId", planId);

        Call<UserRoot> call = RetrofitBuilder.create().ConvertDiamondToCoin(jsonObject);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        sessionManager.saveUser(response.body().getUser());
                        //  double dimonds = rcoin / sessionManager.getSetting().getRCoinForDiamond();
                        String s = "Your " + coin + " Coin Successfully Converted into " + dimonds + " Diamonds";

                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                        incomeBinding.myDiamond.setText(String.valueOf(response.body().getUser().getDiamond()));
                        //popupBuilder.showSimplePopup(s, "Continue", () -> initMain());
                    }
                }
                itemCovertbinding.loder.setVisibility(View.GONE);
                itemCovertbinding.coin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                itemCovertbinding.loder.setVisibility(View.GONE);
            }
        });
    }

    public void getDiamondPlan() {
        Call<DiamondPlanRoot> call = RetrofitBuilder.create().getDiamondPlan();

        call.enqueue(new Callback<DiamondPlanRoot>() {
            @Override
            public void onResponse(Call<DiamondPlanRoot> call, Response<DiamondPlanRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getDiamondPlan().isEmpty()) {
                        Log.d("TAG", "onResponse: success");
                        coinAdapter.clear();
                        coinAdapter.addData(response.body().getDiamondPlan());
                    } else {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvConvertCoin.setVisibility(View.GONE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
            }

            @Override
            public void onFailure(Call<DiamondPlanRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvConvertCoin.setVisibility(View.GONE);
            }
        });

    }


    public interface OnRcoinConvertPopupClickListner {
        void onClickConvert(int rcoin);
    }
}
