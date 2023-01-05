package com.video.buzzy.popupbuilder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ItemSimplepopupBinding;
import com.video.buzzy.databinding.PopupRcoinConvertBinding;
import com.video.buzzy.fragment.ConvertCoinFragment;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

public class PopupBuilder {
    private final Context mContext;
    Dialog mBuilder;
    SessionManager sessionManager;

    public PopupBuilder(Context context) {
        this.mContext = context;
        if (mContext != null) {
            sessionManager = new SessionManager(context);
            mBuilder = new Dialog(mContext);
            mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mBuilder.setCancelable(false);
            mBuilder.setCanceledOnTouchOutside(false);
            if (mBuilder != null && mBuilder.getWindow() != null) {
                mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    public void showRcoinConvertPopup(boolean isCashOut, int maxCoin, ConvertCoinFragment.OnRcoinConvertPopupClickListner onRcoinConvertPopupClickListner) {
        if (mContext == null)
            return;

        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        PopupRcoinConvertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.popup_rcoin_convert, null, false);
        mBuilder.setContentView(binding.getRoot());
        final int[] coin = new int[1];


        if (isCashOut) {
            binding.tvText.setText("How much Rcoin convert Cash?");
            binding.btncountinue.setText("Cash Out");

        } else {
            binding.tvText.setText("How much Rcoin convert into Diamonds?");
            binding.btncountinue.setText("Convert To Diamonds");
        }


        binding.etRcoin.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    coin[0] = Integer.parseInt(s.toString());
                    if (coin[0] < 100) {
                        binding.tvDiamondsValue.setText("Minimum amount is 100 Rcoins");
                        binding.tvDiamondsValue.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                        new Handler(Looper.getMainLooper()).postDelayed(() -> binding.tvDiamondsValue.setTextColor(ContextCompat.getColor(mContext, R.color.yellow)), 1000);

                    } else if (coin[0] > maxCoin) {
                        binding.tvDiamondsValue.setText("You not have enough Rcoins");
                        binding.tvDiamondsValue.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                        new Handler(Looper.getMainLooper()).postDelayed(() -> binding.tvDiamondsValue.setTextColor(ContextCompat.getColor(mContext, R.color.yellow)), 1000);
                    } else {
                        if (isCashOut) {
                            int diamond = coin[0] / 100;
                            binding.tvDiamondsValue.setText("You Will Receive " + String.valueOf(diamond) + Const.Currency);
                        } else {
                            int diamond = coin[0] / 100;
                            binding.tvDiamondsValue.setText("You Will Receive " + String.valueOf(diamond) + " Diamonds");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btncountinue.setOnClickListener(v -> {
            if (binding.etRcoin.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "Please enter value sufficient value ", Toast.LENGTH_SHORT).show();
                return;
            }
            mBuilder.dismiss();
            onRcoinConvertPopupClickListner.onClickConvert(coin[0]);
        });

        binding.btnCancel.setOnClickListener(v -> mBuilder.dismiss());
        mBuilder.show();


    }

    public void showSimplePopup(String s, String btnText) {
        if (mContext == null)
            return;

        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        ItemSimplepopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_simplepopup, null, false);
        mBuilder.setContentView(binding.getRoot());
        binding.tvText.setText(s);
        binding.btncountinue.setText(btnText);
        binding.btncountinue.setOnClickListener(v -> mBuilder.dismiss());
        mBuilder.show();

    }

}
