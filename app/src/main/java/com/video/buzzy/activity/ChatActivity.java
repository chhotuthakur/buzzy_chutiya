package com.video.buzzy.activity;

import static android.provider.MediaStore.MediaColumns.DATA;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.adapter.ChatAdapter;
import com.video.buzzy.databinding.ActivityChatBinding;
import com.video.buzzy.design.Chat;
import com.video.buzzy.fragment.GiftBottomSheetChatFrgament;
import com.video.buzzy.fragment.WalletFragment;
import com.video.buzzy.modelRetrofit.ChatRoot;
import com.video.buzzy.modelRetrofit.ChatThumbList;
import com.video.buzzy.modelRetrofit.ChatTopic;
import com.video.buzzy.modelRetrofit.GiftRoot;
import com.video.buzzy.modelRetrofit.UploadImageRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.viewmodel.GiftViewModel;
import com.video.buzzy.viewmodel.ViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {
    private static final int GALLERY_COVER_CODE = 1002;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final int GALLERY_CODE = 1001;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PERMISSION__WRITE_REQUEST_CODE = 102;
    public static boolean isOPEN = false;
    public static String userid;
    public int start = 0;
    public boolean isLoding = false;
    ActivityChatBinding binding;
    ChatAdapter chatAdapter;
    GiftViewModel giftViewModel;
    String username;
    SessionManager sessionManager;
    String chatTopicId = "";
    Socket socket;
    char first;
    boolean isgift = false;
    ChatThumbList.Chat chatRoot;
    private Chat chat;
    boolean issend = false;
    private BottomSheetDialogFragment bottomSheetDialogFragment;


    private Emitter.Listener chatListner = args -> {
        Log.d("TAG", "chetlister : " + args[0]);
        if (args[0] != null) {
            runOnUiThread(() -> {
                ChatRoot.ChatItem chatUserItem = new Gson().fromJson(args[0].toString(), ChatRoot.ChatItem.class);
                if (chatUserItem != null) {
                    Toast.makeText(ChatActivity.this, "gift sended", Toast.LENGTH_SHORT).show();
                    Log.d("<<<>>>", "gift send");

                    if (!issend) {
                        Log.d(">>>>>>>>", ": chat object  " + chatUserItem.toString());
                        Log.d(">>>>>>>>", "chetlister : " + chatUserItem.getMessage());
                        issend = true;
                        chatAdapter.addSingleMessage(chatUserItem);
                        binding.rvChat.smoothScrollToPosition(0);
                    }
                    //issend = false;

                    //scrollAdapterLogic();
                } else {
                    Log.d(">>>>>>>>", "lister : chet obj null");
                }
            });
        }
    };

    private Uri selectedImage;
    private String picturePath;
    private GiftRoot.GiftItem giftmodel;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        initView();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        sessionManager = new SessionManager(this);
        Const.LOGINUSERID = sessionManager.getUser().getId();

        chatAdapter = new ChatAdapter();
        binding.rvChat.setAdapter(chatAdapter);

        binding.rvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.d("TAG", "onScrolled: can scroll-1   " + recyclerView.canScrollVertically(-1));
                if (!recyclerView.canScrollVertically(-1)) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    Log.d("TAG", "onScrollStateChanged: ");
                    int visibleItemcount = manager.getChildCount();
                    int totalitem = manager.getItemCount();
                    int firstvisibleitempos = manager.findFirstCompletelyVisibleItemPosition();
                    Log.d("TAG", "onScrollStateChanged:firstvisible    " + firstvisibleitempos);
                    Log.d("TAG", "onScrollStateChanged:188 " + totalitem);
                    if (!isLoding && (visibleItemcount + firstvisibleitempos >= totalitem) && firstvisibleitempos >= 0) {
                        isLoding = true;
                        start = start + Const.LIMIT;

                        Log.d("<<<<<<<<<", "onScrolled: " + start + " " + Const.LIMIT);
//                        binding.rvChat.clearFocus();
                        getOldChat();

                    }
                }

            }
        });


//        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                Log.d("TAG", "onScrolled: can scroll-1   " + binding.rvChat.canScrollVertically(-1));
//                Log.d("TAG", "onScrolled: can scroll 1   " + binding.rvChat.canScrollVertically(1));
//                if (!binding.rvChat.canScrollVertically(1)) {
//                    binding.rvChat.scrollToPosition(0);
//                }
//            }
//
//        });

        giftViewModel = ViewModelProviders.of(this, new ViewModelFactory(new GiftViewModel()).createFor()).get(GiftViewModel.class);
        binding.setViewmodel(giftViewModel);
        bottomSheetDialogFragment = new GiftBottomSheetChatFrgament();


        Intent i = getIntent();

        chatRoot = new Gson().fromJson(i.getStringExtra(Const.CHATROOM), ChatThumbList.Chat.class);

        if (chatRoot != null) {
            username = chatRoot.getName();
            userid = chatRoot.getUserId();
        } else {
            userid = i.getStringExtra(Const.CHATUSERId);
            username = i.getStringExtra(Const.CHATUSERNAME);
        }

        // Log.d(">>>>>>>>>>>>>>", "initView: " + chatRoot.getTopic());


        if (!username.equals("") && !username.isEmpty()) {
            first = username.charAt(0);
            binding.username.setText(username);
        }

        createChatTopic();

        binding.gift.setOnClickListener(v -> {
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "giftfragment");
        });

        binding.galleryLay.setOnClickListener(v -> {
            if (binding.photoOption.getVisibility() == View.VISIBLE) {
                binding.photoOption.setVisibility(View.GONE);
                binding.galleryLay.setImageResource(R.drawable.chat_icon);
            } else {
                binding.photoOption.setVisibility(View.VISIBLE);
                binding.galleryLay.setImageResource(R.drawable.close);
            }
        });

        binding.username.setOnClickListener(v -> {
            Intent i1 = new Intent(ChatActivity.this, MainPageActivity.class);
            i1.putExtra(Const.ISFROMCHAT, true);
            i1.putExtra(Const.USERID, userid);
            startActivity(i1);
        });


        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

//        binding.etChat.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
//            try {
//                if (keyboardShown(binding.etChat.getRootView())) {
//                    Log.d("keyboard", "keyboard UP");
//                    binding.rvChat.getLayoutManager().scrollToPosition(chatAdapter.getItemCount() - 1);
//                } else {
//                    Log.d("keyboard", "keyboard Down");
//                }
//            } catch (Exception e) {
//            }
//        });

        binding.camera.setOnClickListener(v -> {
            if (checkWritePermission()) {
                if ((checkSelfPermission(Manifest.permission.CAMERA)) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else {
                requestWritePermission();
            }
        });


        binding.gallery.setOnClickListener(v -> {
            choosePhoto();
        });


        giftViewModel.finalgift.observe(this, gift -> {
            Log.d(">>>>>>>", "initData: home " + gift.getImage());
            giftmodel = gift;
            //sendGift(reelsItem.getId(), gift.getId());
        });


        giftViewModel.isSend.observe(this, aBoolean -> {
            if (aBoolean) {
                Log.d("TAG", "initView: " + aBoolean);
                try {
                    bottomSheetDialogFragment.dismiss();

                    if (giftmodel.getCoin() > sessionManager.getUser().getCoin()) {
                        Log.d("coin", "OnSendGiftClick: if ");
                        Toast.makeText(this, "You coin is not enough", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    initSocketIO();
                    sendGift();

                } catch (Exception e) {
                }
            }

        });

    }

    public void onRechargeClick() {
        changeFragment(new WalletFragment());

    }

    private void changeFragment(Fragment homeFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.host, homeFragment).commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendGift() {

        if (giftmodel != null) {

            if (giftmodel.getId().isEmpty()) {
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("senderId", sessionManager.getUser().getId());
                jsonObject.put("messageType", 2);
                jsonObject.put("topic", chatTopicId);
                jsonObject.put("message", "");
                jsonObject.put("giftId", giftmodel.getId());
                jsonObject.put("coin", giftmodel.getCoin());

                Log.d("TAG", "initListner: send chat " + jsonObject);

                Toast.makeText(ChatActivity.this, "Send Gift", Toast.LENGTH_SHORT).show();

                issend = false;

                socket.emit(Const.EVENT_CHAT, jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void choosePhoto() {
        if (checkPermission()) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_CODE);
        } else {
            requestPermission();
        }

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkWritePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    private void requestWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION__WRITE_REQUEST_CODE);
        }
    }


    private void scrollAdapterLogic() {

        if (binding.rvChat.canScrollVertically(1)) {
            //binding.btnScroll.setVisibility(View.VISIBLE);
        } else {
            binding.rvChat.scrollToPosition(0);
        }

        hideKeyboard(ChatActivity.this);
    }


    private boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


    public void createChatTopic() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("senderUserId", sessionManager.getUser().getId());
        jsonObject.addProperty("receiverUserId", userid);

        Call<ChatTopic> call = RetrofitBuilder.create().createChatTopic(jsonObject);

        call.enqueue(new Callback<ChatTopic>() {
            @Override
            public void onResponse(Call<ChatTopic> call, Response<ChatTopic> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body() != null) {
                    chatTopicId = response.body().getChatTopic().getId();
                    Log.d(">>>>>>>>", "onResponse: " + chatTopicId);

                    initSocketIO();
                    initListner();
                    getOldChat();
                }
            }

            @Override
            public void onFailure(Call<ChatTopic> call, Throwable t) {
                Log.d(">>>>>>>>", "onFailure: " + t.getMessage());
            }
        });

    }

    private void initSocketIO() {
        IO.Options options = IO.Options.builder()
                // IO factory options
                .setForceNew(false)
                .setMultiplex(true)

                // low-level engine options
                .setTransports(new String[]{Polling.NAME, WebSocket.NAME})
                .setUpgrade(true)
                .setRememberUpgrade(false)
                .setPath("/socket.io/")
                .setQuery("chatRoom=" + chatTopicId + "")
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
        socket = IO.socket(uri, options);
        Log.d(">>>>>>>>", "onCreate: " + socket.id());
        socket.connect();

        socket.on("connection", args -> Log.d("TAG", "call: "));

        socket.on(Socket.EVENT_CONNECT, args -> {
            Log.d(">>>>>>>>", "call: connect" + args.length);
            socket.on(Const.EVENT_CHAT, chatListner);

        });


    }


    private void initListner() {
        binding.send.setOnClickListener(v -> {

            String message = binding.etChat.getText().toString();
            if (message.isEmpty()) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("senderId", sessionManager.getUser().getId());
                jsonObject.put("messageType", 0);
                jsonObject.put("topic", chatTopicId);
                jsonObject.put("message", binding.etChat.getText().toString().trim());
                Log.d("TAG", "initListner: send chat " + jsonObject);

                Log.d(">>>>>>>>", "initListner: senderid" + sessionManager.getUser().getId());
                Log.d(">>>>>>>>", "initListner: msgtype" + 0);
                Log.d(">>>>>>>>", "initListner: topic" + chatTopicId);
                Log.d(">>>>>>>>", "initListner: message" + binding.etChat.getText().toString().trim());

                issend = false;
                socket.emit(Const.EVENT_CHAT, jsonObject);
                binding.etChat.setText("");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            selectedImage = data.getData();

            Log.d("TAG", "onActivityResult: " + selectedImage);

            // Glide.with(this).load(selectedImage).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(false).into(binding.userImg);

            String[] filePathColumn = {DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            Log.d("TAG", "picpath:2 " + picturePath);

            issend = false;
            sendImage();

            //  sessionManager.saveStringValue(Const.IMAGEPATH, picturePath);

            Log.d("TAG", "onActivityResultpicpath: " + picturePath);
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            selectedImage = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            picturePath = getRealPathFromURI(selectedImage);

            // picturePath  = String.valueOf(data.getData());

            // imageView.setImageBitmap(photo);

//            String[] filePathColumn = {DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            Log.d("TAG", "picpath:2 " + picturePath);

            issend = false;
            sendImage();

        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public void getOldChat() {

        Log.d("<<<<<<<<<", "getOldChat: " + start + " " + Const.LIMIT);
        Call<ChatRoot> call = RetrofitBuilder.create().getOldChat(chatTopicId, start, Const.LIMIT);

        call.enqueue(new Callback<ChatRoot>() {
            @Override
            public void onResponse(Call<ChatRoot> call, Response<ChatRoot> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    if (!response.body().getChat().isEmpty()) {
                        chatAdapter.addData(response.body().getChat());
                        chatAdapter.setRecieverText(String.valueOf(first).toUpperCase(Locale.ROOT));
                        isLoding = false;
                    }

                }
            }

            @Override
            public void onFailure(Call<ChatRoot> call, Throwable t) {

            }

        });
    }

    public void sendImage() {
        if (picturePath != null) {

            Log.d("TAG", "sendImage: " + picturePath);

            File file = new File(picturePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            RequestBody messageTypebody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1));
            RequestBody topicbody = RequestBody.create(MediaType.parse("text/plain"), chatTopicId);
            RequestBody senderIdbody = RequestBody.create(MediaType.parse("text/plain"), sessionManager.getUser().getId());

            HashMap<String, RequestBody> map = new HashMap<>();

            map.put("messageType", messageTypebody);
            map.put("topic", topicbody);
            map.put("senderId", senderIdbody);

            Call<UploadImageRoot> call = RetrofitBuilder.create().sendChatViaImage(map, body);
            call.enqueue(new Callback<UploadImageRoot>() {
                @Override
                public void onResponse(Call<UploadImageRoot> call, Response<UploadImageRoot> response) {
                    if (response.code() == 200) {
                        if (response.body().isStatus()) {
                            //  binding.lytImage.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("senderId", sessionManager.getUser().getId());
                                jsonObject.put("messageType", 1);
                                jsonObject.put("topic", chatTopicId);
                                jsonObject.put("message", "image");
                                jsonObject.put("date", response.body().getChat().getDate());
                                jsonObject.put("image", response.body().getChat().getImage());

                                Log.d("TAG", "initListner: send chat " + jsonObject);

                                socket.emit(Const.EVENT_CHAT, jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadImageRoot> call, Throwable t) {

                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        socket.disconnect();
        super.onDestroy();
    }
}
