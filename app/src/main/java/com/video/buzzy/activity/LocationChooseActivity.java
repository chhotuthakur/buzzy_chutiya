package com.video.buzzy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.video.buzzy.R;
import com.video.buzzy.adapter.LocationAdapter;
import com.video.buzzy.databinding.ActivityLocationChooseBinding;
import com.video.buzzy.modelRetrofit.LocationRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationChooseActivity extends BaseActivity {
    // private Call<SearchLocationRoot> call;
    public static final int REQ_CODE_LOCATION = 123;
    ActivityLocationChooseBinding binding;
    LocationAdapter locationAdapter = new LocationAdapter();
    private String keyword = "";
    private int start = 0;
    private Call<LocationRoot> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_choose);
        Intent intent = getIntent();
        String location = intent.getStringExtra(Const.DATA);
        if (location != null && !location.isEmpty()) {
            binding.etLocation.setText(location);
            searchLocation();
        } else {
            searchLocation();
        }

        binding.btnDone.setOnClickListener(v -> {
            Intent i = getIntent();
            i.putExtra(Const.DATA, binding.etLocation.getText().toString().trim());
            setResult(RESULT_OK, i);
            finish();
        });

        binding.rvLocation.setAdapter(locationAdapter);


        locationAdapter.setOnLocationClickLisnter(selectedLocation -> {
            Intent i = getIntent();

            i.putExtra(Const.DATA, new Gson().toJson(selectedLocation));
            setResult(RESULT_OK, i);
            finish();
        });

        binding.etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (call != null) {
                    call.cancel();
                }

                binding.rvLocation.setVisibility(View.GONE);
                keyword = cs.toString();
                searchLocation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etLocation.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(binding.etLocation.getWindowToken(), 0);

                if (call != null) {
                    call.cancel();
                }

                binding.etLocation.clearFocus();
                keyword = binding.etLocation.getText().toString();
                searchLocation();

            }
            return true;
        });


    }

    public void onClickBack(View view) {
        finish();
    }

    private void searchLocation() {
        if (call != null) {
            call.cancel();
        }

        if (keyword.isEmpty()) {
            binding.nodata.setVisibility(View.GONE);
            locationAdapter.clear();
        }

        call = RetrofitBuilder.create().getLocationList(keyword);
        call.enqueue(new Callback<LocationRoot>() {
            @Override
            public void onResponse(Call<LocationRoot> call, Response<LocationRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getResponse().isEmpty()) {
                        binding.rvLocation.setVisibility(View.VISIBLE);
                        binding.nodata.setVisibility(View.GONE);
                        locationAdapter.clear();
                        locationAdapter.addData(response.body().getResponse());
                    }
                    if (response.body().getResponse().isEmpty()) {
                        binding.nodata.setVisibility(View.VISIBLE);
                        binding.rvLocation.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<LocationRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());

            }
        });
    }
}