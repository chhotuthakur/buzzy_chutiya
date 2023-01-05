package com.video.buzzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivityWebviewBinding;
import com.video.buzzy.util.Const;


public class WebActivity extends BaseActivity {
    ActivityWebviewBinding binding;
    Intent i;
    String url;
    private String websitename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);

        binding.webView.getSettings().setJavaScriptEnabled(true);

        i = getIntent();
        url = i.getStringExtra(Const.URL);

        Log.d(">>>>", "onCreate: " + url);
        if (!url.equals("")) {
            LoadUrl();

            websitename = getbaseWebsite(url);
            binding.websiteName.setText(websitename);

        }


    }

    private String getbaseWebsite(String url) {
        try {
            String[] s = url.split("//");
            String[] s1 = s[1].split("/");
            return s1[0];
        } catch (Exception e) {
            return url;
        }
    }


    public void LoadUrl() {
        binding.webView.getSettings().setSupportZoom(false);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setSupportMultipleWindows(true);
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webView.getSettings().setAllowFileAccess(false);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setDisplayZoomControls(false);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // binding.animation.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                Log.d(">>>>", "onPageFinished: ");
                // binding.animation.setVisibility(View.GONE);
            }
        });

        binding.webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void BackClick(View view) {
        finish();
    }
}