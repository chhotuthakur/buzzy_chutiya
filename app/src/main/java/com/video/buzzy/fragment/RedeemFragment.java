package com.video.buzzy.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.video.buzzy.R;
import com.video.buzzy.adapter.RedeemCoinAdapter;
import com.video.buzzy.adapter.ReedemMethodAdapter;
import com.video.buzzy.databinding.FragmentMyIncomeFragmentBinding;
import com.video.buzzy.databinding.FragmentRedeemBinding;
import com.video.buzzy.modelRetrofit.RedeemDiamondRoot;
import com.video.buzzy.modelRetrofit.RedeemPlanRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemFragment extends Fragment {
    FragmentRedeemBinding binding;
    RedeemCoinAdapter redeemCoinAdapter;
    int myRcoin = 3000;
    SessionManager sessionManager;
    List<String> paymentGateways = new ArrayList<>();
    private String selectedPaymentGateway;
    private com.video.buzzy.databinding.FragmentMyIncomeFragmentBinding incomeBinding;

    public RedeemFragment() {
    }

    public RedeemFragment(FragmentMyIncomeFragmentBinding incomeBinding) {
        this.incomeBinding = incomeBinding;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_redeem, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        sessionManager = new SessionManager(getActivity());

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();

        redeemCoinAdapter = new RedeemCoinAdapter();
        binding.rvConvertCoin.setAdapter(redeemCoinAdapter);

        getRedeemPlan();

        redeemCoinAdapter.setOnClickReedem(redeemPlanItem -> {
//            PopupBuilder popupBuilder = new PopupBuilder(getActivity());
//            popupBuilder.showRcoinConvertPopup(true, myRcoin, rcoin -> {
//                double cash = rcoin / 100;
//                String s = "Your " + rcoin + " Successfully Redeem Your " + cash + " Diamonds";
//                popupBuilder.showSimplePopup(s, "Continue");
//            });

            if (sessionManager.getUser().getDiamond() >= redeemPlanItem.getDiamond()) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.cash_out_dialog);

                TextView diamond = (TextView) dialog.findViewById(R.id.diamond);
                TextView btnSubmit = (TextView) dialog.findViewById(R.id.btnSubmit);
                RecyclerView rvReedemMethods = (RecyclerView) dialog.findViewById(R.id.rvReedemMethods);
                EditText etDetails = (EditText) dialog.findViewById(R.id.etDetails);
                ImageView close = (ImageView) dialog.findViewById(R.id.close);

                diamond.setText(redeemPlanItem.getDiamond() + " " + "Diamond");

                paymentGateways.clear();
                paymentGateways.addAll(sessionManager.getSetting().getPaymentGateway());

                if (paymentGateways != null && !paymentGateways.isEmpty()) {
                    // changeDetails(paymentGateways.get(0));
                    etDetails.setHint("Enter Your " + paymentGateways.get(0) + " " + "details");
                    rvReedemMethods.setAdapter(new ReedemMethodAdapter(paymentGateways, s -> {
                        selectedPaymentGateway = s;
                        if (s.equalsIgnoreCase("UPI")) {
                            etDetails.setHint("Enter your UPI details");
                        } else if (s.equalsIgnoreCase("Banking")) {
                            etDetails.setHint("Enter your Banking details");
                        } else {
                            etDetails.setHint("Enter your account details");
                        }
                    }));

                } else {
                    Toast.makeText(getActivity(), "No Payment Method Found", Toast.LENGTH_SHORT).show();
                }


                close.setOnClickListener(v -> dialog.dismiss());

                btnSubmit.setOnClickListener(v -> {
                    if (selectedPaymentGateway == null || selectedPaymentGateway.isEmpty()) {
                        Toast.makeText(getActivity(), "No Payment Method Found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String des = etDetails.getText().toString();
                    if (des.isEmpty()) {
                        Toast.makeText(getActivity(), "Please Enter Your Details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Cashout(selectedPaymentGateway, redeemPlanItem.getId(), des);
                    dialog.dismiss();
                });

                dialog.show();

            } else {
                Toast.makeText(getActivity(), "Your haven't enough diamonds for reedeem.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getRedeemPlan() {
        Call<RedeemPlanRoot> call = RetrofitBuilder.create().getRedeemPlan();

        call.enqueue(new Callback<RedeemPlanRoot>() {
            @Override
            public void onResponse(Call<RedeemPlanRoot> call, Response<RedeemPlanRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getRedeemPlan().isEmpty()) {
                        Log.d("TAG", "onResponse: success");
                        redeemCoinAdapter.addData(response.body().getRedeemPlan());
                    } else {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvConvertCoin.setVisibility(View.GONE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();
                }
            }

            @Override
            public void onFailure(Call<RedeemPlanRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvConvertCoin.setVisibility(View.GONE);
            }
        });
    }


    public void Cashout(String paymentGateway, String planId, String des) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", sessionManager.getUser().getId());
        jsonObject.addProperty("paymentGateway", paymentGateway);
        jsonObject.addProperty("planId", planId);
        jsonObject.addProperty("description", des);

        Call<RedeemDiamondRoot> call = RetrofitBuilder.create().RedeemDiamond(jsonObject);

        call.enqueue(new Callback<RedeemDiamondRoot>() {
            @Override
            public void onResponse(Call<RedeemDiamondRoot> call, Response<RedeemDiamondRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getUser() != null) {
                    Log.d("TAG", "onResponse: success");
                    Toast.makeText(getActivity(), "Successfully requested for redeem....", Toast.LENGTH_SHORT).show();
                    incomeBinding.myDiamond.setText(String.valueOf(response.body().getUser().getDiamond()));
                }
            }

            @Override
            public void onFailure(Call<RedeemDiamondRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


}
