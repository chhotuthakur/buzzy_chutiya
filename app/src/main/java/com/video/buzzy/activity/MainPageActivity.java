package com.video.buzzy.activity;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.NavGraphDirections;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivityMainPageBinding;
import com.video.buzzy.fragment.HomeFragment;
import com.video.buzzy.fragment.ReelsOfUserFragment;
import com.video.buzzy.fragment.UserProfileFragment;
import com.video.buzzy.fragment.WalletFragment;
import com.video.buzzy.modelRetrofit.ReelsVideoRoot;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.ReelsViewModel;
import com.video.buzzy.viewmodel.ViewModelFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends BaseActivity {
    ActivityMainPageBinding binding;
    NavController navController;
    boolean isHome = false, isSearch = false, isChat = false, isProfile = false;
    SessionManager sessionManager;
    boolean serviceintent, servicereel = false, isfromchat = false;
    String userid;
    private boolean ishome = false;
    private FragmentManager fragmentManager;
    private ReelsViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_page);
        initView();

    }

    private void initView() {
        sessionManager = new SessionManager(this);

        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new ReelsViewModel()).createFor()).get(ReelsViewModel.class);

        Intent intent = getIntent();
        String branchData = intent.getStringExtra(Const.DATA);
        String type = intent.getStringExtra(Const.TYPE);

        serviceintent = intent.getBooleanExtra(Const.SEARVICEINTENT, false);
        servicereel = intent.getBooleanExtra(Const.SERVICEREEL, false);


        Log.d(">>>>>", "initView:  intent" + serviceintent);
        Log.d(">>>>>", "initView:  reel" + servicereel);

        if (serviceintent) {
            changeFragment(new UserProfileFragment());
            return;
        } else if (servicereel) {
            changeFragment(new ReelsOfUserFragment());
            return;
        }

        if (branchData != null && !branchData.isEmpty()) {
            if (type != null && !type.isEmpty()) {
                if (type.equals("RELITE")) {
                    ReelsVideoRoot.Reel post = new Gson().fromJson(branchData, ReelsVideoRoot.Reel.class);
                    List<ReelsVideoRoot.Reel> list = new ArrayList<>();
                    list.add(post);
                    viewModel.isFromBranch.setValue(true);

                    Bundle newBundle = new Bundle();
                    newBundle.putString(Const.DATA, new Gson().toJson(list));
                    newBundle.putInt(Const.POSITION, 0);
                    HomeFragment objects = new HomeFragment();
                    objects.setArguments(newBundle);

                    binding.home.performClick();
                    // startActivity(new Intent(this, HomeFragment.class).putExtra(Const.POSITION, 0).putExtra(Const.DATA, new Gson().toJson(list)));
                }
            }
        }

        binding.home.setImageTintList(ContextCompat.getColorStateList(this, R.color.pink));
        binding.txtHome.setTextColor(ContextCompat.getColorStateList(this, R.color.pink));
        navController = findNavController();

        makeUserOnline();
        createUserStatusSocket();

        binding.homeLay.setOnClickListener(v -> {
            ishome = true;
            setDefault();
            setFalse();
            navController.navigate(NavGraphDirections.actionHome());
            binding.home.setImageTintList(ContextCompat.getColorStateList(this, R.color.pink));
            binding.txtHome.setTextColor(ContextCompat.getColorStateList(this, R.color.pink));
            ishome = true;
        });

        binding.searchLay.setOnClickListener(v -> {
            setDefault();
            setFalse();
            navController.navigate(NavGraphDirections.actionSearch());
            binding.search.setImageTintList(ContextCompat.getColorStateList(this, R.color.pink));
            binding.txtSearch.setTextColor(ContextCompat.getColorStateList(this, R.color.pink));
            isSearch = true;
        });

        binding.plusLay.setOnClickListener(v -> {
            Intent i = new Intent(this, RecorderActivity.class);
            startActivity(i);
        });

        binding.chatLay.setOnClickListener(v -> {
            setDefault();
            setFalse();

            navController.navigate(NavGraphDirections.actionChat());
            binding.chat.setImageTintList(ContextCompat.getColorStateList(this, R.color.pink));
            binding.txtChat.setTextColor(ContextCompat.getColorStateList(this, R.color.pink));
            isChat = true;
        });

        binding.profileLay.setOnClickListener(v -> {
            setDefault();
            setFalse();
            navController.navigate(NavGraphDirections.actionMainProfile());
            binding.profile.setImageTintList(ContextCompat.getColorStateList(this, R.color.pink));
            binding.txtProfile.setTextColor(ContextCompat.getColorStateList(this, R.color.pink));
            isProfile = true;
        });

    }

    private void setDefault() {
        binding.home.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
        binding.search.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
        binding.chat.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
        binding.profile.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
        binding.txtHome.setTextColor(ContextCompat.getColor(this, R.color.icon_color));
        binding.txtSearch.setTextColor(ContextCompat.getColor(this, R.color.icon_color));
        binding.txtChat.setTextColor(ContextCompat.getColor(this, R.color.icon_color));
        binding.txtProfile.setTextColor(ContextCompat.getColor(this, R.color.icon_color));
    }

    private void setFalse() {
        isHome = false;
        isSearch = false;
        isChat = false;
        isProfile = false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showExitDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);

        yes.setOnClickListener(v -> {
            finish();
            dialog.dismiss();
        });

        no.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();
    }


    public NavController findNavController() {
        NavHostFragment fragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.host);
        return fragment.getNavController();
    }


    public void makeUserOnline() {
        Call<UserRoot> call = RetrofitBuilder.create().makeUserOnline(sessionManager.getUser().getId());

        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getUser() != null) {
                    Log.d("TAG", "onResponse: user is online now ");
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


    private void createUserStatusSocket() {
        IO.Options options = IO.Options.builder()
                // IO factory options
                .setForceNew(false)
                .setMultiplex(true)

                // low-level engine options
                .setTransports(new String[]{Polling.NAME, WebSocket.NAME})
                .setUpgrade(true)
                .setRememberUpgrade(false)
                .setPath("/socket.io/")
                .setQuery("userRoom=" + sessionManager.getUser().getId() + "")
                .setExtraHeaders(null)

                // Manager options
                .setReconnection(true)
                .setReconnectionAttempts(Integer.MAX_VALUE)
                .setReconnectionDelay(1_000)
                .setReconnectionDelayMax(5_000)
                .setRandomizationFactor(0.5)
                .setTimeout(20_000)

                // Socket options
                .setAuth(null)
                .build();

        URI uri = URI.create(BuildConfig.BASE_URL);
        Socket callSocket = IO.socket(uri, options);
        callSocket.connect();

        callSocket.on(Socket.EVENT_CONNECT, args -> {
            Log.e("TAG", "createUserStatusSocket: connected");
        });

    }

    private void changeFragment(Fragment homeFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.host, homeFragment).commit();
    }

    public void openRecharge() {
        changeFragment(new WalletFragment());
    }

    @Override
    protected void onResume() {
        Intent i = getIntent();
        userid = i.getStringExtra(Const.USERID);
        isfromchat = i.getBooleanExtra(Const.ISFROMCHAT, false);


        if (isfromchat && (!userid.isEmpty() && userid != null)) {
            changeFragment(new UserProfileFragment());
        }

        super.onResume();
    }
}