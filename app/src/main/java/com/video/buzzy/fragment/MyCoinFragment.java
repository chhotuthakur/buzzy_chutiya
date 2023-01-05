package com.video.buzzy.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.video.buzzy.R;
import com.video.buzzy.adapter.CoinAdapter;
import com.video.buzzy.ads.MyRewardAds;
import com.video.buzzy.databinding.BottomSheetCardBinding;
import com.video.buzzy.databinding.FragmentMyCoinFragmentBinding;
import com.video.buzzy.databinding.ItemTotalCoinBinding;
import com.video.buzzy.databinding.PaymentBottomsheetDialogBinding;
import com.video.buzzy.modelRetrofit.CoinPlanRoot;
import com.video.buzzy.modelRetrofit.StripePaymentRoot;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoinFragment extends Fragment implements MyRewardAds.RewardAdListnear {
    FragmentMyCoinFragmentBinding binding;
    CoinAdapter coinAdapter;
    PaymentBottomsheetDialogBinding paymentsheetbinding;
    BottomSheetDialog bottomSheetDialog;
    SessionManager sessionManager;
    MyRewardAds myRewardAds;
    BillingClient billingClient;
    CoinPlanRoot.CoinPlanItem selectedPlan;
    BottomSheetCardBinding bottomSheetCardBinding;
    int userCoin;
    private boolean apiCalled = false;
    private String TAG = "coinfragment";
    private String planId;
    private BottomSheetDialog bottomSheetDialog2;
    private PaymentMethodCreateParams params;
    private Stripe stripe;
    private String currency;
    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
            Log.d(TAG, "onPurchasesUpdated: 1");
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                Log.d(TAG, "onPurchasesUpdated: size  " + purchases.size());
                Log.d(TAG, "onPurchasesUpdated: success");
                if (!purchases.isEmpty()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Log.d(TAG, "run: success");
                            handlePurchase(purchases.get(0));
                        });
                    }
                }
                for (Purchase purchase : purchases) {
                    Toast.makeText(getActivity(), "thy gyu", Toast.LENGTH_SHORT).show();
                    binding.userCoin.setText(String.valueOf(userCoin));

                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                Log.d(TAG, "onPurchasesUpdated: error");
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }
    };

    public MyCoinFragment() {
    }

    void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchasesAsync or your PurchasesUpdatedListener.

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.


        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        if (!apiCalled) {
            Log.d(TAG, "handlePurchase: qwetuioooi2wqwertyukiol==================");
            apiCalled = true;
            callGooglePay(purchase);

        } else {
            Log.d(TAG, "handlePurchase: sdsd");
        }


        ConsumeResponseListener listener = (billingResult, purchaseToken) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "handlePurchase: consume");
                // Handle the success of the consume operation.
            }
        };

        billingClient.consumeAsync(consumeParams, listener);

    }

    private void callGooglePay(Purchase purchase) {
        if (getActivity() == null) return;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", sessionManager.getUser().getId());
        jsonObject.addProperty("planId", selectedPlan.getId());
        jsonObject.addProperty("productId", selectedPlan.getProductKey());
        jsonObject.addProperty("packageName", getActivity().getPackageName());
        jsonObject.addProperty("token", purchase.getPurchaseToken());

        Call<UserRoot> call = RetrofitBuilder.create().callGooglePay(jsonObject);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200) {

                    if (response.body().isStatus() && response.body().getUser() != null) {
                        Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
                        sessionManager.saveUser(response.body().getUser());
                        binding.userCoin.setText(String.valueOf(response.body().getUser().getCoin()));

                    } else {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }

                    apiCalled = false;
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: failed" + t.getMessage());
            }
        });
    }

    private void setUpSku(String productid) {
        List<String> skuList = new ArrayList<>();

        //  skuList.add("tanzanite");
//         skuList.add("android.test.purchased");

        skuList.add(productid);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    // Process the result.
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Log.d(TAG, "run: " + skuDetailsList.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (skuDetailsList.isEmpty()) {
                                Toast.makeText(getActivity(), "Purchase error", Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                lunchPayment(skuDetailsList.get(0));
                            }

                        });
                    } else {
                        Log.d(TAG, "setUpSku: get act is null");
                    }

                });
    }

    private void lunchPayment(SkuDetails s) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(s)
                .build();
        int responseCode = billingClient.launchBillingFlow(getActivity(), billingFlowParams).getResponseCode();

        Log.d(TAG, "lunchPayment: " + responseCode);
    }


    private void StripeSelected() {
        bottomSheetDialog2 = new BottomSheetDialog(getActivity(), R.style.CustomBottomSheetDialogTheme);
        bottomSheetDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheetCardBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottom_sheet_card, null, false);
        bottomSheetDialog2.setContentView(bottomSheetCardBinding.getRoot());
        bottomSheetCardBinding.cardInputWidget.setPostalCodeEnabled(false);
        bottomSheetCardBinding.cardInputWidget.setPostalCodeRequired(false);
        bottomSheetCardBinding.btnclose.setOnClickListener(v -> bottomSheetDialog2.dismiss());
        bottomSheetCardBinding.tvamount.setText(String.valueOf(selectedPlan.getRupee()));

        Log.d(TAG, "StripeSelected: " + selectedPlan.getRupee());

        bottomSheetCardBinding.btnPay.setOnClickListener(v -> {
            bottomSheetDialog2.dismiss();
            // binding.pd.setVisibility(View.VISIBLE);

            CardInputWidget cardInputWidget = bottomSheetCardBinding.cardInputWidget;
            cardInputWidget.setPostalCodeRequired(false);
            cardInputWidget.setPostalCodeEnabled(false);
            params = cardInputWidget.getPaymentMethodCreateParams();

            if (params == null) {
                Log.d(TAG, "stripeSelected: aaa ");
                return;
            }

            stripe = new Stripe(getActivity(), PaymentConfiguration.getInstance(getActivity()).getPublishableKey());
            stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                @RequiresApi(api = Build.VERSION_CODES.R)
                @Override
                public void onSuccess(@NonNull PaymentMethod result) {
                    Log.d(TAG, "onSuccess: ");
                    pay(result.id, null);
                    // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
                }

                @Override
                public void onError(@NonNull Exception e) {
                    Log.d(TAG, "onError: " + e.getMessage());
                }
            });

        });

        bottomSheetDialog2.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message, boolean b) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message);

        builder.setPositiveButton("Ok", (dialog, which) -> {

        });
        builder.create().show();
    }

    private void stripePurchasedDone(JsonObject jsonObject) {
        Call<UserRoot> call = RetrofitBuilder.create().purchsePlanStripeDiamons(jsonObject);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.body().isStatus()) {

                    Toast.makeText(getActivity(), "plan purchased", Toast.LENGTH_SHORT).show();
                    sessionManager.saveUser(response.body().getUser());
                    binding.userCoin.setText(String.valueOf(response.body().getUser().getCoin()));

                } else {

                    Toast.makeText(getActivity(), "Purchase error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public void pay(@Nullable String paymentMethodId, @Nullable String paymentIntentId) {
        // ...continued in the next step

        currency = "INR";

        JsonObject jsonObject = new JsonObject();
        if (paymentMethodId != null) {
            jsonObject.addProperty("payment_method_id", paymentMethodId);
            jsonObject.addProperty("userId", sessionManager.getUser().getId());
            jsonObject.addProperty("planId", selectedPlan.getId());
            jsonObject.addProperty("currency", currency.toLowerCase());
            stripePurchased(jsonObject);
        } else {
            jsonObject.addProperty("payment_intent_id", paymentIntentId);
            jsonObject.addProperty("userId", sessionManager.getUser().getId());
            jsonObject.addProperty("planId", selectedPlan.getId());
            jsonObject.addProperty("currency", currency.toLowerCase());
            stripePurchasedDone(jsonObject);
        }


    }

    private void initView() {
        myRewardAds = new MyRewardAds(getActivity(), this);

        sessionManager = new SessionManager(getActivity());

        userCoin = sessionManager.getUser().getCoin();

        PaymentConfiguration.init(
                getActivity(),
                sessionManager.getSetting().getStripePublishableKey());

        binding.shimmar.setVisibility(View.VISIBLE);
        binding.shimmar.startShimmer();


        coinAdapter = new CoinAdapter();
        binding.rvCoin.setAdapter(coinAdapter);

        coinAdapter.setOnClickItem(new CoinAdapter.OnClickItem() {
            @Override
            public void onClick(CoinPlanRoot.CoinPlanItem coinPlanItem) {
                Log.d(TAG, "onClick: onplan selected ");
                selectedPlan = coinPlanItem;
                initListner();
            }

            @Override
            public void onAdsClick(CoinPlanRoot.CoinPlanItem coinPlanItem, ItemTotalCoinBinding coinbinding) {
                Log.d(TAG, "onAdsClick:  onad selected");
                binding.pd.setVisibility(View.VISIBLE);
                coinbinding.adsLay.setVisibility(View.GONE);

                Log.d(TAG, "onAdsClick: " + myRewardAds.isLoaded);

                new Handler().postDelayed(() -> {
                    if (myRewardAds.isLoaded) {
                        coinbinding.adsLay.setVisibility(View.VISIBLE);
                        binding.pd.setVisibility(View.GONE);
                        myRewardAds.showAds(getActivity());
                    }
                }, 3000);
            }

        });

        billingClient = BillingClient.newBuilder(getActivity())
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d(TAG, "onBillingSetupFinished: ");
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // binding.tvMyCoins.setText(String.valueOf(sessionManager.getUser().getDiamond()));
                                getCoinPlan();
                                //initListner();

                            }
                        });
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d(TAG, "onBillingServiceDisconnected: ");
            }
        });


        binding.userCoin.setText(String.valueOf(sessionManager.getUser().getCoin()));
    }

    private void stripePurchased(JsonObject jsonObject) {
        Call<StripePaymentRoot> call = RetrofitBuilder.create().callStripePay(jsonObject);
        call.enqueue(new Callback<StripePaymentRoot>() {
            @Override
            public void onResponse(Call<StripePaymentRoot> call, Response<StripePaymentRoot> response) {
                if (response.code() == 200 && response.body().isStatus()) {
                    if (response.body().getPaymentIntentClientSecret() != null) {
                        if (getActivity() != null) {

                            getActivity().runOnUiThread(() ->
                                    stripe.handleNextActionForPayment(getActivity(), response.body().getPaymentIntentClientSecret()));
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Purchse error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StripePaymentRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_coin_fragment, container, false);
        initView();
        return binding.getRoot();
    }

    private void addCoin() {
        Call<UserRoot> call = RetrofitBuilder.create().getCoinViewingAds(sessionManager.getUser().getId());
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.body().isStatus()) {
                    Log.d("<<<<<<<", "onResponse: success");
                    sessionManager.saveUser(response.body().getUser());
                    binding.userCoin.setText(String.valueOf(response.body().getUser().getCoin()));
                    Toast.makeText(getActivity(), "Earned by User", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d("<<<<<<<<", "onFailure: addCoin " + t.getMessage());
            }

        });
    }

    private void initListner() {
        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CustomBottomSheetDialogTheme);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        paymentsheetbinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.payment_bottomsheet_dialog, null, false);
        bottomSheetDialog.setContentView(paymentsheetbinding.getRoot());

        if (sessionManager.getSetting().isGooglePlaySwitch()) {
            paymentsheetbinding.gpayLay.setVisibility(View.VISIBLE);
        } else paymentsheetbinding.gpayLay.setVisibility(View.GONE);

        if (sessionManager.getSetting().isStripeSwitch())
            paymentsheetbinding.stripeLay.setVisibility(View.VISIBLE);
        else paymentsheetbinding.stripeLay.setVisibility(View.GONE);


        paymentsheetbinding.gpayLay.setOnClickListener(v -> {
            Log.d(TAG, "initListner gpay selected... ");
            bottomSheetDialog.dismiss();
            if (billingClient.isReady()) {
                Log.d(TAG, "onClick: fd " + selectedPlan.getId());
                userCoin = userCoin + selectedPlan.getCoin();
                planId = selectedPlan.getId();
                setUpSku(selectedPlan.getProductKey());
            } else {
                Log.d("TAG", "paymetMethord: bp not init");
            }
        });


        paymentsheetbinding.stripeLay.setOnClickListener(v -> {
            Log.d(TAG, "initListner: stripe sleected");
            bottomSheetDialog.dismiss();
            StripeSelected();
        });

        bottomSheetDialog.show();

    }

    public void getCoinPlan() {
        Call<CoinPlanRoot> call = RetrofitBuilder.create().getPlanRoot();

        call.enqueue(new Callback<CoinPlanRoot>() {
            @Override
            public void onResponse(Call<CoinPlanRoot> call, Response<CoinPlanRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.d("TAG", "onResponse: success");

                    if (!response.body().getCoinPlan().isEmpty()) {
                        if (sessionManager.getSetting().getFreeCoinForAd() != 0) {
                            CoinPlanRoot.CoinPlanItem coinPlanItem = new CoinPlanRoot.CoinPlanItem();
                            coinPlanItem.setCoin(sessionManager.getSetting().getFreeCoinForAd());
                            // coinPlanItem.setRupee(0);
                            response.body().getCoinPlan().add(0, coinPlanItem);
                        }
                        coinAdapter.addData(response.body().getCoinPlan());
                    } else {
                        binding.nodataLay.setVisibility(View.VISIBLE);
                        binding.rvCoin.setVisibility(View.GONE);
                    }
                    binding.shimmar.setVisibility(View.GONE);
                    binding.shimmar.stopShimmer();

                }
            }

            @Override
            public void onFailure(Call<CoinPlanRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: getCoinPlan " + t.getMessage());
                binding.shimmar.setVisibility(View.GONE);
                binding.shimmar.stopShimmer();
                binding.nodataLay.setVisibility(View.VISIBLE);
                binding.rvCoin.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onAdClosed() {
        Log.d("<<<<<<<<<", "onAdClosed: ");
    }

    @Override
    public void onEarned() {
        Log.d("<<<<<<<<<", "onEarned: ");
        addCoin();
        myRewardAds = new MyRewardAds(getActivity(), this);
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<MyCoinFragment> activityRef;

        PaymentResultCallback(@NonNull MyCoinFragment activity) {
            activityRef = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final MyCoinFragment activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert("Payment completed",
                        gson.toJson(paymentIntent), true);
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert("Payment failed",
                        paymentIntent.getLastPaymentError().getMessage(), false);
            } else if (status == PaymentIntent.Status.RequiresConfirmation) {
                // After handling a required action on the client, the status of the PaymentIntent is
                // requires_confirmation. You must send the PaymentIntent ID to your backend
                // and confirm it to finalize the payment. This step enables your integration to
                // synchronously fulfill the order on your backend and return the fulfillment result
                // to your client.
                activity.pay(null, paymentIntent.getId());
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final MyCoinFragment activity = activityRef.get();
            if (activity == null) {
                return;
            }

            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString(), false);
        }
    }

}
