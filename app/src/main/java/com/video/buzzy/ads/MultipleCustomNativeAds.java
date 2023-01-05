package com.video.buzzy.ads;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.video.buzzy.databinding.ItemCustomAdsBinding;
import com.video.buzzy.databinding.ItemGoogleAdAdapterBinding;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

@Keep
public class MultipleCustomNativeAds {
    private final Context context;

    boolean loadMoreAds = true;
    int offset;
    int index;
    SessionManager sessionManager;
    boolean istheme = false;
    String TAG = "adnatice";
    private OnLoadAds onLoadAds;


    public MultipleCustomNativeAds(Context context, OnLoadAds onLoadAds, int offset) {
        this.context = context;
        this.onLoadAds = onLoadAds;
        this.offset = offset;
        index = offset - 1;
        initAds();

    }

    private void initAds() {
        sessionManager = new SessionManager(context);

        if (sessionManager.getBooleanValue(Const.AdsDownload)) {
            Log.d(TAG, "initAds: ");
            if (sessionManager.getAds() != null && sessionManager.getAds().isShow()) {
                loadNativeAds();
            }
        }

    }


    private void loadNativeAds() {
        if (loadMoreAds) {
            AdLoader.Builder builder = null;
            builder = new AdLoader.Builder(context, sessionManager.getAds().getJsonMemberNative());

            Log.d(TAG, "loadNativeAds: " + sessionManager.getAds().getJsonMemberNative());

            AdLoader adLoader = builder.forNativeAd(nativeAd -> {
                Log.d(TAG, "onNativeAdLoaded: ");
                loadMoreAds = onLoadAds.onLoad(nativeAd, index);
                index = index + offset;
                MultipleCustomNativeAds.this.loadNativeAds();
            }).withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                }
            }).withNativeAdOptions(new NativeAdOptions.Builder()
                    .setRequestCustomMuteThisAd(true)
                    .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                    .build()).build();
            adLoader.loadAds(new AdRequest.Builder().build(), 1);

        }
    }


    public interface OnLoadAds {
        boolean onLoad(Object adsData, int position);
    }

    public static class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {
        SessionManager sessionManager;
        Context context;
        ItemGoogleAdAdapterBinding binding;
        private NativeAdView adView;

        public UnifiedNativeAdViewHolder(View view) {
            super(view);

            context = view.getContext();
            sessionManager = new SessionManager(view.getContext());

            binding = ItemGoogleAdAdapterBinding.bind(view);

            if (sessionManager.getAds() != null && sessionManager.getAds().isShow() && sessionManager.getAds().getJsonMemberNative() != null) {
                adView = (NativeAdView) binding.adView;

                // The MediaView will display a video asset if one is present in the ad, and the
                // first image asset otherwise.

                adView.setMediaView(binding.adMedia);

                // Register the view used for each individual asset.

                adView.setHeadlineView(binding.adTitle);
                adView.setBodyView(binding.adDescription);
                adView.setCallToActionView(binding.btnApply);
                adView.setIconView(binding.adAppIcon);
                adView.setPriceView(binding.adPrice);
                adView.setStarRatingView(binding.adStars);
                adView.setStoreView(binding.adStore);
                adView.setAdvertiserView(binding.adAdvertiser);

            }
        }


        public NativeAdView getAdView() {
            return adView;
        }



        public void populateNativeAdView(NativeAd nativeAd,
                                         NativeAdView adView) {
            // Some assets are guaranteed to be in every UnifiedNativeAd.
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.

            NativeAd.Image icon = nativeAd.getIcon();

//            if (icon == null) {
//                adView.getIconView().setVisibility(View.INVISIBLE);
//            } else {
//                Glide.with(adView.getIconView())
//                        .load(icon.getDrawable())
//                        .circleCrop()
//                        .into((ImageView) adView.getIconView());
//                adView.getIconView().setVisibility(View.VISIBLE);
//            }

            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }

            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }

            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            // Assign native ad object to the native view.
            adView.setNativeAd(nativeAd);
        }

    }

    public static class CustomAdViewHolder extends RecyclerView.ViewHolder {
        private final ItemCustomAdsBinding custombinding;
        SessionManager sessionManager;
        Context context;
        SimpleExoPlayer player;

        public CustomAdViewHolder(View view) {
            super(view);
            custombinding = ItemCustomAdsBinding.bind(view);
            context = view.getContext();
            sessionManager = new SessionManager(view.getContext());

        }

        public void setData() {


        }

        public void stopPlayer() {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                    player.release();
                    player.setPlayWhenReady(false);

                }
            }
        }

    }


}

