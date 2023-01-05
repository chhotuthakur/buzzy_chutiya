package com.video.buzzy.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.activity.CommentPostActivity;
import com.video.buzzy.activity.WebActivity;
import com.video.buzzy.adapter.ReelsAdapter;
import com.video.buzzy.databinding.FragmentHomeBinding;
import com.video.buzzy.databinding.ItemCustomAdsBinding;
import com.video.buzzy.databinding.ItemReelsBinding;
import com.video.buzzy.modelRetrofit.AllVideoRoot;
import com.video.buzzy.modelRetrofit.CustomAdsRoot;
import com.video.buzzy.modelRetrofit.ReelsLikeRoot;
import com.video.buzzy.modelRetrofit.SearchUserRoot;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.other.MainApplication;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.GiftViewModel;
import com.video.buzzy.viewmodel.ReelsViewModel;
import com.video.buzzy.viewmodel.ViewModelFactory;

import java.util.Calendar;
import java.util.Random;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements Player.EventListener {

    FragmentHomeBinding binding;
    ReelsViewModel viewModel;
    NavController navController;
    int like;
    GiftViewModel giftViewModel;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    Animation animation;
    AllVideoRoot.ReelItem reels;
    SessionManager sessionManager;
    boolean isLiked = false;
    SimpleExoPlayer player;
    ItemReelsBinding playerBinding;
    int lastPosition = 0;
    Animation rotate_animation;
    Call<SearchUserRoot> call;
    AllVideoRoot.ReelItem reelsItem;
    boolean isLoding = false;
    String branchStr, branchPos;
    ItemReelsBinding reelsbinding;
    private int start = 0;
    private ItemCustomAdsBinding googleplayerBinding;


    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new ReelsViewModel()).createFor()).get(ReelsViewModel.class);

        viewModel.initModel(getActivity());

        initView();
        initListner();

        if (getArguments() != null) {
            branchStr = getArguments().getString(Const.DATA);
            branchPos = getArguments().getString(Const.POSITION);
            viewModel.getReliteData(false, sessionManager.getUser().getId(), binding, branchStr);
        } else {
            viewModel.getReliteData(false, sessionManager.getUser().getId(), binding, "");
        }

        return binding.getRoot();
    }

    private void initListner() {

        binding.swipeRefresh.autoLoadMore();

        binding.swipeRefresh.setOnRefreshListener(refreshLayout -> viewModel.getReliteData(false, sessionManager.getUser().getId(), binding, ""));
        binding.swipeRefresh.setOnLoadMoreListener(refreshLayout -> {
            viewModel.getReliteData(true, sessionManager.getUser().getId(), binding, "");
        });

        viewModel.isLoadCompleted.observe(getActivity(), aBoolean -> {
            binding.swipeRefresh.finishRefresh();
            binding.swipeRefresh.finishLoadMore();
            viewModel.isFirstTimeLoading.set(false);
            viewModel.isLoadMoreLoading.set(false);
        });

        binding.rvReels.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = ((LinearLayoutManager) binding.rvReels.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    if (!(position <= -1) && lastPosition != position) {
                        if (binding.rvReels.getLayoutManager() != null) {
                            View view = binding.rvReels.getLayoutManager().findViewByPosition(position);
                            if (view != null) {
                                lastPosition = position;

                                if (viewModel.reelsAdapter.getList().get(position) instanceof AllVideoRoot.ReelItem) {
                                    Log.d("<<<TAG>>>", "onScrollStateChanged: reel");
                                    ItemReelsBinding binding1 = DataBindingUtil.bind(view);
                                    if (binding1 != null) {
                                        binding1.lytSound.startAnimation(rotate_animation);

                                        playVideo(BuildConfig.BASE_URL + ((AllVideoRoot.ReelItem) viewModel.reelsAdapter.getList().get(position)).getVideo(), binding1);
                                        setThumbnail(BuildConfig.BASE_URL + ((AllVideoRoot.ReelItem) viewModel.reelsAdapter.getList().get(position)).getVideo(), binding1);
                                    }

                                } else if (viewModel.reelsAdapter.getList().get(position) == null) {
                                    Log.d("<<<TAG>>>", "onScrollStateChanged: custom");

                                    ItemCustomAdsBinding custombinding = DataBindingUtil.bind(view);
                                    setCustomAd(custombinding);
                                } else {
                                    if (player != null) {
                                        player.setPlayWhenReady(false);
                                        player.release();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        giftViewModel.finalgift.observe(getActivity(), gift -> {
            Log.d(">>>>>>>", "initData: home " + gift.getImage());

            if (gift.getCoin() > sessionManager.getUser().getCoin()) {
                Log.d("coin", "OnSendGiftClick: if ");
                Toast.makeText(getContext(), "You coin is not enough", Toast.LENGTH_SHORT).show();
                return;
            }

            sendGift(reelsItem.getId(), gift.getId());

            if ((getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED)) {
                binding.gift.setVisibility(View.VISIBLE);
                //binding.gift.startAnimation(animation);
                Glide.with(binding.getRoot()).load(BuildConfig.BASE_URL + gift.getImage()).into(binding.gift);
                new Handler().postDelayed(() -> binding.gift.setVisibility(View.GONE), 2000);
            }

        });


        giftViewModel.isSend.observe(getActivity(), aBoolean -> {
            if (aBoolean) {
                Log.d("TAG", "initView: " + aBoolean);
                try {
                    bottomSheetDialogFragment.dismiss();
                } catch (Exception e) {
                }
            }
        });

        viewModel.reelsAdapter.setOnReelsVideoAdapterListner(new ReelsAdapter.OnReelsVideoAdapterListner() {
            @Override
            public void onItemClick(ItemReelsBinding reelsBinding, int pos, int type) {
                reelsbinding = reelsBinding;
                if (type == 1) {
                    lastPosition = pos;
                    playVideo(BuildConfig.BASE_URL + ((AllVideoRoot.ReelItem) viewModel.reelsAdapter.getList().get(pos)).getVideo(), reelsBinding);
                    setThumbnail(BuildConfig.BASE_URL + ((AllVideoRoot.ReelItem) viewModel.reelsAdapter.getList().get(pos)).getVideo(), reelsBinding);
                } else {
                    if (player != null) {
                        if (player.isPlaying()) {
                            reelsBinding.play.setVisibility(View.VISIBLE);
                            reelsBinding.pause.setVisibility(View.GONE);

                            // new Handler().postDelayed(() -> reelsBinding.pause.setVisibility(View.GONE), 2000);

                            player.setPlayWhenReady(false);

                        } else {

                            reelsBinding.play.setVisibility(View.GONE);
                            reelsBinding.pause.setVisibility(View.VISIBLE);

                            new Handler().postDelayed(() -> reelsBinding.pause.setVisibility(View.GONE), 2000);
                            player.setPlayWhenReady(true);
                        }
                    }
                }
            }

            @Override
            public void onDoubleClick(AllVideoRoot.ReelItem model, MotionEvent event, ItemReelsBinding binding) {
                showHeart(event, binding);
                if (!model.isIsLike()) {
                    binding.like.performClick();
                }
            }

            @Override
            public void onClickLike(ItemReelsBinding reelsBinding, int pos) {
                getReelsLiked(((AllVideoRoot.ReelItem) viewModel.reelsAdapter.getList().get(pos)).getId(), reelsBinding, pos);
            }

            @Override
            public void onClickUser(AllVideoRoot.ReelItem reel) {
                reels = reel;
                Bundle b = new Bundle();
                b.putString(Const.USERIDFORGETPROFILE, reel.getUser().getId());
                navController.navigate(R.id.action_homeFragment_to_userProfileFragment, b);
            }

            @Override
            public void onClickComments(AllVideoRoot.ReelItem reels) {
                Intent i = new Intent(requireActivity(), CommentPostActivity.class);
                i.putExtra(Const.REELSID, reels.getId());
                startActivity(i);
            }

            @Override
            public void onClickShare(AllVideoRoot.ReelItem reel) {
                shareRlite(reel);
            }


            @Override
            public void onClickGift(AllVideoRoot.ReelItem reelItem) {
                bottomSheetDialogFragment.show(getChildFragmentManager(), "giftfragment");
                reelsItem = reelItem;
            }

            @Override
            public void onClickProduct(AllVideoRoot.ReelItem reelItem) {
                Intent i = new Intent(getActivity(), WebActivity.class);
                i.putExtra(Const.URL, reelItem.getProductUrl());
                startActivity(i);
            }

            @Override
            public void onClickHashtag(String text) {
                Bundle b = new Bundle();
                b.putString(Const.SEARCHHASHTAGTEXT, text);
                navController.navigate(R.id.action_homeFragment_to_viewhashtagfragment, b);
            }

            @Override
            public void onClickMention(String text) {
                if (!text.equals("") && !text.isEmpty()) {
                    Bundle b = new Bundle();
                    b.putString(Const.FINDUSERPROFILEBYNAME, text);
                    navController.navigate(R.id.action_homeFragment_to_userProfileFragment, b);
                }
            }
        });

        binding.getSuggested.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_trendinguserfragment);
        });

        binding.wallet.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_walletFragment);
        });


    }

    private void setCustomAd(ItemCustomAdsBinding custombinding) {
        CustomAdsRoot.CustomAdItem CustomItem = sessionManager.getOwnads().get(0);

        custombinding.title.setText(CustomItem.getTitle());
        custombinding.description.setText(CustomItem.getDescription());

        custombinding.tag.setText(CustomItem.getProductTag());

        custombinding.shope.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), WebActivity.class);
            i.putExtra(Const.URL, CustomItem.getProductUrl());
            startActivity(i);
        });

        Glide.with(getActivity()).load(BuildConfig.BASE_URL + CustomItem.getProductImage()).into(custombinding.shope);

        Glide.with(getActivity()).load(BuildConfig.BASE_URL + CustomItem.getPublisherLogo()).into(custombinding.publisherLogo);

        Glide.with(getActivity()).asBitmap()
                .load(BuildConfig.BASE_URL + CustomItem.getVideo())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 5)))
                .into(custombinding.thumbnailVideo);

        custombinding.publisherName.setText(CustomItem.getPublisherName());


        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
        }

        Log.d("TAG", "playVideo:URL  " + BuildConfig.BASE_URL + CustomItem.getVideo());

        player = new SimpleExoPlayer.Builder(getActivity()).build();
        SimpleCache simpleCache = MainApplication.simpleCache;
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(simpleCache, new DefaultHttpDataSourceFactory(Util.getUserAgent(getActivity(), "TejTok"))
                , CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        ProgressiveMediaSource progressiveMediaSource = new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(Uri.parse(BuildConfig.BASE_URL + CustomItem.getVideo()));
        custombinding.playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.seekTo(0, 0);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d("TAG", "onPlayerError: something went wrong");

            }
        });
        custombinding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        // MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));
        player.prepare(progressiveMediaSource, true, false);
        // playVideo(BuildConfig.BASE_URL + CustomItem.getVideo(),custombinding);

    }

    private void initView() {
        sessionManager = new SessionManager(getActivity());

        bottomSheetDialogFragment = new GiftBottomSheetFrgament();

        giftViewModel = ViewModelProviders.of(this, new ViewModelFactory(new GiftViewModel()).createFor()).get(GiftViewModel.class);
        binding.setViewModel(viewModel);

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        rotate_animation = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.slow_rotate);
        navController = Navigation.findNavController(requireActivity(), R.id.host);

        binding.rvReels.setAdapter(viewModel.reelsAdapter);
        new PagerSnapHelper().attachToRecyclerView(binding.rvReels);


        // getReels();

//        binding.swipeLayout.setOnRefreshListener(() -> {
//            viewModel.reelsAdapter.clear();
//            getReels();
//        });


    }

    public void sendGift(String reelId, String giftId) {
        JsonObject jsonObject = new JsonObject();

        Log.d("TAG", "sendGift: login user id " + sessionManager.getUser().getId());

        jsonObject.addProperty("userId", sessionManager.getUser().getId());
        jsonObject.addProperty("reelId", reelId);
        jsonObject.addProperty("gift", giftId);


        Call<UserRoot> call = RetrofitBuilder.create().sendGift(jsonObject);

        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getUser() != null) {
                    sessionManager.saveUser(response.body().getUser());
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


    private void shareRlite(AllVideoRoot.ReelItem reel) {
        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle(reel.getCaption())
                .setContentDescription("By : " + reel.getUser().getName())
                .setContentImageUrl(BuildConfig.BASE_URL + reel.getScreenshot())
                .setContentMetadata(new ContentMetadata().addCustomMetadata("type", "RELITE").addCustomMetadata(Const.DATA, new Gson().toJson(reel)));

        LinkProperties lp = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .setCampaign("content 123 launch")
                .setStage("new user")
                .addControlParameter("$desktop_url", "http://example.com/home")
                .addControlParameter("", "")
                .addControlParameter("", Long.toString(Calendar.getInstance().getTimeInMillis()));

        buo.generateShortUrl(getActivity(), lp, (url, error) -> {
            Log.d("TAG", "initListnear: branch url" + url);
            try {
                Log.d("TAG", "initListnear: share");
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareMessage = url;
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                Log.d("TAG", "initListnear: " + e.getMessage());
                //e.toString();
            }
        });
    }


    public void showHeart(MotionEvent e, ItemReelsBinding binding) {

        int x = (int) e.getX() - 200;
        int y = (int) e.getY() - 200;
        Log.i("TAG", "showHeart: " + x + "------" + y);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(getActivity());
        lp.setMargins(x, y, 0, 0);
        iv.setLayoutParams(lp);
        Random r = new Random();
        int i1 = r.nextInt(30 + 30) - 30;
        iv.setRotation(i1);
        iv.setImageResource(R.drawable.ic_heart_gradient);
        if (binding.rtl.getChildCount() > 0) {
            binding.rtl.removeAllViews();
        }
        binding.rtl.addView(iv);
        Animation fadeoutani = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        fadeoutani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.rtl.removeView(iv);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(fadeoutani);

    }


    public void getReelsLiked(String reelsId, ItemReelsBinding reelsBinding, int pos) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", sessionManager.getUser().getId());
        jsonObject.addProperty("reelId", reelsId);

        Call<ReelsLikeRoot> call = RetrofitBuilder.create().getReelsLikedOrNot(jsonObject);

        call.enqueue(new Callback<ReelsLikeRoot>() {
            @Override
            public void onResponse(Call<ReelsLikeRoot> call, Response<ReelsLikeRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    AllVideoRoot.ReelItem model = ((AllVideoRoot.ReelItem) viewModel.reelsAdapter.getList().get(pos));

                    int like;

                    if (response.body().isIsLike()) {
                        like = model.getLike() + 1;
                    } else {
                        like = model.getLike() - 1;
                    }
                    reelsBinding.like.setLiked(response.body().isIsLike());

                    reelsBinding.likeCount.setText(String.valueOf(like));

                    model.setIsLike(response.body().isIsLike());
                    model.setLike(like);
                    viewModel.reelsAdapter.notifyItemChanged(pos, model);
                }
            }

            @Override
            public void onFailure(Call<ReelsLikeRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

//    public void getReels() {
//        Call<AllVideoRoot> call = RetrofitBuilder.create().getAllReelsVideo(sessionManager.getUser().getId());
//
//        call.enqueue(new Callback<AllVideoRoot>() {
//            @Override
//            public void onResponse(Call<AllVideoRoot> call, Response<AllVideoRoot> response) {
//                if (response.code() == 200 && response.isSuccessful()) {
//                    if (!response.body().getReel().isEmpty()) {
//                        viewModel.reelsAdapter.clear();
//                        // binding.nodataLay.setVisibility(View.GONE);
//                        viewModel.reelsAdapter.addData(response.body().getReel());
//                    } else {
//                        // binding.nodataLay.setVisibility(View.VISIBLE);
//                        binding.rvReels.setVisibility(View.GONE);
//                        binding.buffering.setVisibility(View.GONE);
//                    }
//                }
//                binding.swipeLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(Call<AllVideoRoot> call, Throwable t) {
//                Log.d("TAG", "onFailure: " + t.getMessage());
//                binding.swipeLayout.setRefreshing(false);
//                binding.rvReels.setVisibility(View.GONE);
//                // binding.nodataLay.setVisibility(View.VISIBLE);
//                binding.buffering.setVisibility(View.GONE);
//            }
//        });
//    }


    private void setThumbnail(String video, ItemReelsBinding reelsBinding) {
        Glide.with(reelsBinding.getRoot())
                .load(video)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 5)))
                .into(reelsBinding.thumbnailVideo);
    }


    private void playVideo(String videoUrl, ItemReelsBinding binding) {
        if (player != null) {
            player.removeListener(this);
            player.setPlayWhenReady(false);
            player.release();
        }
        Log.d("TAG", "playVideo:URL  " + videoUrl);
        playerBinding = binding;
        player = new SimpleExoPlayer.Builder(getActivity()).build();
        SimpleCache simpleCache = MainApplication.simpleCache;
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(simpleCache, new DefaultHttpDataSourceFactory(Util.getUserAgent(getActivity(), "TejTok"))
                , CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        ProgressiveMediaSource progressiveMediaSource = new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(Uri.parse(videoUrl));
        binding.playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.seekTo(0, 0);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);
        binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        // MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));
        player.prepare(progressiveMediaSource, true, false);

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//        if (viewModel.reelsAdapter.getList().size() > 0) {
//            binding.buffering.setVisibility(View.GONE);
//            return;
//        }
        if (playbackState == Player.STATE_BUFFERING) {
            //  binding.pd.setVisibility(View.VISIBLE);
            if (playerBinding != null) {
                binding.buffering.setVisibility(View.VISIBLE);
            }
        } else if (playbackState == Player.STATE_READY) {
            //   binding.pd.setVisibility(View.GONE);
            if (playerBinding != null) {
                binding.buffering.setVisibility(View.GONE);
            }
        }

    }

    public void showWallet() {
        navController = Navigation.findNavController(requireActivity(), R.id.host);
        navController.navigate(R.id.action_homeActivityFragment_to_walletFragment);
    }

    @Override
    public void onResume() {
        Log.d("<<<TAG>>>", "onResume: ");
//        viewModel.reelsAdapter.clear();
        //getReels();

        if (player != null) {
            player.setPlayWhenReady(true);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d("<<<TAG>>>", "onStop: ");
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.d("<<<TAG>>>", "onPause: ");
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("<<<TAG>>>", "onDestroy: ");
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.release();
        }
        super.onDestroy();

    }


//    public void showWallet() {
//        navController = Navigation.findNavController(requireActivity(), R.id.host);
//        navController.navigate(R.id.action_homeActivityFragment_to_walletFragment);
//    }


}
