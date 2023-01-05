package com.video.buzzy.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.video.buzzy.R;
import com.video.buzzy.activity.ChatActivity;
import com.video.buzzy.adapter.GiftAdapter;
import com.video.buzzy.databinding.FragmentGiftBottomsheetBinding;
import com.video.buzzy.modelRetrofit.GiftRoot;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.GiftViewModel;

public class GiftBottomSheetChatFrgament extends BottomSheetDialogFragment {
    FragmentGiftBottomsheetBinding binding;
    GiftViewModel giftViewModel;
    SessionManager sessionManager;
    DialogInterface dialogbottom;


    public GiftBottomSheetChatFrgament() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gift_bottomsheet, container, false);
        giftViewModel = ViewModelProviders.of(getActivity()).get(GiftViewModel.class);
        binding.setViewModel(giftViewModel);
        initData();
        return binding.getRoot();
    }

    private void initData() {

        sessionManager = new SessionManager(getActivity());

        binding.rvGift.setAdapter(giftViewModel.giftAdapter);

        giftViewModel.getGift();

        binding.userCoin.setText(String.valueOf(sessionManager.getUser().getCoin()));

        binding.rechargeLay.setOnClickListener(v -> {
            ((ChatActivity) getActivity()).onRechargeClick();
            //ChatFragment.isfromrecharge = true;
        });


        giftViewModel.giftAdapter.setOnGiftClick(new GiftAdapter.OnGiftClick() {
            @Override
            public void onGiftClick(GiftRoot.GiftItem gift) {
                Log.d(">>>>>>>", "initData: sheet " + gift.getImage());
                giftViewModel.Selectedgift.setValue(gift);
                Log.d(">>>>>>>", "initData: " + gift.toString());
            }

            @Override
            public void OnSendGiftClick(GiftRoot.GiftItem gift) {

                if (gift.getCoin() > sessionManager.getUser().getCoin()) {
                    Log.d("coin", "OnSendGiftClick: if ");
                    Toast.makeText(getContext(), "You coin is not enough", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (giftViewModel.Selectedgift.getValue() != null) {
                    Log.d(">>>>>>>", "initData: if ");
                    Log.d(">>>>>>>", "initData: if" + giftViewModel.Selectedgift.getValue().toString());

                    giftViewModel.finalgift.setValue(giftViewModel.Selectedgift.getValue());

                } else {
                    Log.d(">>>>>>>>", "initData: else ");
                    giftViewModel.Selectedgift.setValue(null);
                    giftViewModel.finalgift.setValue(null);
                }

                giftViewModel.giftAdapter.refreshSelectValue();
                giftViewModel.isSend.postValue(true);
            }
        });

        giftViewModel.Selectedgift.observe(this, gift -> {
            Log.d(">>>>>>", "initData: " + gift.toString());
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        this.dialogbottom = dialog;
    }
}
