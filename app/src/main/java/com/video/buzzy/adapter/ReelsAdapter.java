package com.video.buzzy.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.nativead.NativeAd;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.ads.MultipleCustomNativeAds;
import com.video.buzzy.databinding.ItemReelsBinding;
import com.video.buzzy.modelRetrofit.AllVideoRoot;
import com.video.buzzy.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ReelsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEWTYPE = 2;
    private static final int AD_TYPE = 1;
    private static final int CUSTOM_TYPE = 3;
    OnReelsVideoAdapterListner onReelsVideoAdapterListner;
    Context context;
    SessionManager sessionManager;
    boolean isStop = false;
    private int playAtPosition = 0;
    private List<Object> reels = new ArrayList<>();

    public OnReelsVideoAdapterListner getOnReelsVideoAdapterListner() {
        return onReelsVideoAdapterListner;
    }

    public void setOnReelsVideoAdapterListner(OnReelsVideoAdapterListner onReelsVideoAdapterListner) {
        this.onReelsVideoAdapterListner = onReelsVideoAdapterListner;
    }

    @Override
    public int getItemViewType(int position) {
        if (reels.get(position) == null) {
            return CUSTOM_TYPE;
        } else if (reels.get(position) instanceof com.google.android.gms.ads.nativead.NativeAd) {
            return AD_TYPE;
        } else {
            return VIEWTYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == AD_TYPE) {
            Log.d("TAG", "onCreateViewHolder: google  type");
            View unifiedNativeLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.item_google_ad_adapter,
                    parent, false);
            return new MultipleCustomNativeAds.UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
        } else if (viewType == CUSTOM_TYPE) {
            Log.d("TAG", "onCreateViewHolder:  customttype");
            View unifiedNativeLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.item_custom_ads,
                    parent, false);
            return new MultipleCustomNativeAds.CustomAdViewHolder(unifiedNativeLayoutView);
        } else {
            sessionManager = new SessionManager(context);
            return new ReelsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reels, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MultipleCustomNativeAds.UnifiedNativeAdViewHolder) {
            Log.d("TAG", "onBindViewHolder: google viewholder");
            MultipleCustomNativeAds.UnifiedNativeAdViewHolder viewHolder = (MultipleCustomNativeAds.UnifiedNativeAdViewHolder) holder;
            com.google.android.gms.ads.nativead.NativeAd nativeAd = (NativeAd) reels.get(position);
            viewHolder.populateNativeAdView(nativeAd, ((MultipleCustomNativeAds.UnifiedNativeAdViewHolder) holder).getAdView());
        } else if (holder instanceof MultipleCustomNativeAds.CustomAdViewHolder) {
            MultipleCustomNativeAds.CustomAdViewHolder viewHolder = (MultipleCustomNativeAds.CustomAdViewHolder) holder;
            viewHolder.setData();
        } else {
            Log.d("TAG", "onBindViewHolder: data viewholder");
            ((ReelsAdapter.ReelsViewHolder) holder).setdata(position);
        }

    }

    @Override
    public int getItemCount() {
        return reels.size();
    }

    public void addNewAds(int index, com.google.android.gms.ads.nativead.NativeAd ad) {
        if (!reels.isEmpty() && index < reels.size()) {
            reels.add(index, ad);
            notifyItemInserted(index);
        }
    }

    public List<Object> getList() {
        return reels;
    }

    public void addData(List<AllVideoRoot.ReelItem> reels) {
        this.reels.addAll(reels);
        notifyItemRangeInserted(this.reels.size(), reels.size());
    }

    public void playVideoAt(int position) {
        playAtPosition = position;
        notifyDataSetChanged();
    }


    public void clear() {
        reels.clear();
        notifyDataSetChanged();
    }

    public void addCustom(int position) {
        reels.add(position, null);
        notifyDataSetChanged();
    }

    public interface OnReelsVideoAdapterListner {
        void onItemClick(ItemReelsBinding reelsBinding, int pos, int type);

        void onDoubleClick(AllVideoRoot.ReelItem model, MotionEvent event, ItemReelsBinding binding);

        void onClickLike(ItemReelsBinding reelsBinding, int pos);

        void onClickUser(AllVideoRoot.ReelItem reel);

        void onClickComments(AllVideoRoot.ReelItem reels);

        void onClickShare(AllVideoRoot.ReelItem reel);

        void onClickGift(AllVideoRoot.ReelItem reelItem);

        void onClickProduct(AllVideoRoot.ReelItem reelItem);

        void onClickHashtag(String text);

        void onClickMention(String text);

    }

    public class ReelsViewHolder extends RecyclerView.ViewHolder {
        ItemReelsBinding binding;

        public ReelsViewHolder(View itemView) {
            super(itemView);
            binding = ItemReelsBinding.bind(itemView);
        }

        public void setdata(int position) {
            AllVideoRoot.ReelItem reel = (AllVideoRoot.ReelItem) reels.get(position);


            if (reel.getUser().getId().equalsIgnoreCase(sessionManager.getUser().getId())) {
                binding.giftLay.setVisibility(View.GONE);
            } else binding.giftLay.setVisibility(View.VISIBLE);


            Glide.with(binding.getRoot()).load(reel.getUser().getProfileImage()).into(binding.thumbnail);
            binding.username.setText(reel.getUser().getName());
            binding.email.setText(reel.getUser().getUsername());

            if (position == playAtPosition) {
                Animation animation = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.slow_rotate);
                onReelsVideoAdapterListner.onItemClick(binding, playAtPosition, 1);
                binding.lytSound.startAnimation(animation);
            }

            binding.like.setLiked(reel.isIsLike());
            binding.likeCount.setText(String.valueOf(reel.getLike()));

            if (reel.getCaption() == null)
                binding.captionLay.setVisibility(View.GONE);
            else {
                binding.captionLay.setVisibility(View.VISIBLE);
                binding.bio.setText(reel.getCaption());
                binding.bio.setHashtagColors(ColorStateList.valueOf(context.getResources().getColor(R.color.light_blue)));
                binding.bio.setMentionColors(ColorStateList.valueOf(context.getResources().getColor(R.color.light_blue)));
            }

            if (reel.isIsProductShow()) {
                binding.shopLay.setVisibility(View.VISIBLE);
                Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + reel.getProductImage()).placeholder(R.drawable.shop).into(binding.shope);
                Log.d(">>>>", "setData: " + BuildConfig.BASE_URL + reel.getProductImage());
                binding.tag.setText(reel.getProductTag());

            } else binding.shopLay.setVisibility(View.GONE);

            binding.shopLay.setOnClickListener(v -> {
                onReelsVideoAdapterListner.onClickProduct(reel);
            });

            if (reel.getSong() == null) binding.songName.setText("Original Audio..");
            else
                binding.songName.setText(reel.getSong().getTitle() + "" + reel.getSong().getSinger());

            binding.likeCount.setText(String.valueOf(reel.getLike()));
            binding.commentCount.setText(String.valueOf(reel.getComment()));

            if (position == playAtPosition) {
                Animation animation = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.slow_rotate);
                onReelsVideoAdapterListner.onItemClick(binding, playAtPosition, 1);
                binding.lytSound.startAnimation(animation);
            }

            binding.thumbnail.setOnClickListener(v -> onReelsVideoAdapterListner.onClickUser(reel));
            binding.comment.setOnClickListener(v -> onReelsVideoAdapterListner.onClickComments(reel));

            binding.bio.setOnHashtagClickListener((view, text) -> onReelsVideoAdapterListner.onClickHashtag(text.toString()));

            binding.bio.setOnMentionClickListener((view, text) -> {
                onReelsVideoAdapterListner.onClickMention(text.toString());
            });

            binding.giftLay.setOnClickListener(v -> {
                onReelsVideoAdapterListner.onClickGift(reel);
            });

            binding.comment.setOnClickListener(v -> {
                onReelsVideoAdapterListner.onClickComments(reel);
            });

            binding.share.setOnClickListener(v -> {
                onReelsVideoAdapterListner.onClickShare(reel);
            });

            binding.like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    onReelsVideoAdapterListner.onClickLike(binding, position);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    onReelsVideoAdapterListner.onClickLike(binding, position);
                }
            });

            binding.playerView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(binding.getRoot().getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        Log.d("TAGA", "onSingleTapUp: ");

                        return true;
                    }

                    @Override
                    public void onShowPress(MotionEvent e) {
                        Log.d("TAGA", "onShowPress: ");
                        super.onShowPress(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        Log.d("TAGA", "onSingleTapConfirmed: ");
                        onReelsVideoAdapterListner.onItemClick(binding, position, 2);
                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        // onReelsVideoAdapterListner.onItemClick(reel, position, 8, binding);
                        super.onLongPress(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Log.d("TAGA", "onDoubleTap: ");
                        onReelsVideoAdapterListner.onDoubleClick(reel, e, binding);
                        return true;
                    }
                });


                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return false;
                }

            });

        }
    }

}
