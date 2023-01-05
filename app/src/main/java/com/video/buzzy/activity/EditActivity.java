package com.video.buzzy.activity;

import static android.provider.MediaStore.MediaColumns.DATA;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.video.buzzy.R;
import com.video.buzzy.databinding.ActivityEditBinding;
import com.video.buzzy.modelRetrofit.UserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends BaseActivity {

    private static final int GALLERY_COVER_CODE = 1002;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final int GALLERY_CODE = 1001;
    ActivityEditBinding binding;
    SessionManager sessionManager;
    private Uri selectedImage;
    private String picturePath, pictureCoverPath;
    private Uri selectedCoverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        iniView();
    }

    private void iniView() {

        binding.etUsername.setEnabled(false);

        openEditSheet();

        sessionManager = new SessionManager(this);

        binding.coverImag.setVisibility(View.VISIBLE);
        binding.coverLay.setOnClickListener(v -> chooseCoverPhoto());

        binding.close.setOnClickListener(v -> finish());
        binding.done.setOnClickListener(v -> finish());

        binding.etName.setText(sessionManager.getUser().getName());
        binding.etUsername.setText(sessionManager.getUser().getUsername());
        binding.etBio.setText(sessionManager.getUser().getBio());

        Log.d("TAG", "iniView: profile " + sessionManager.getUser().getProfileImage());
        Log.d("TAG", "iniView: cover " + sessionManager.getUser().getCoverImage());


        if (!sessionManager.getUser().getProfileImage().isEmpty()) {
            Glide.with(this).load(sessionManager.getUser().getProfileImage()).into(binding.userImg);
            binding.imgUser.setVisibility(View.GONE);
        } else binding.imgUser.setVisibility(View.VISIBLE);


        if (sessionManager.getUser().getCoverImage() != null) {
            Glide.with(this).load(sessionManager.getUser().getCoverImage()).into(binding.coverImag);
        }


        binding.done.setOnClickListener(v -> {
            String name = binding.etName.getText().toString();
            String username = binding.etUsername.getText().toString();
            String bio = binding.etBio.getText().toString();

            binding.etUsername.setEnabled(false);

            if (name.isEmpty()) {
                Toast.makeText(EditActivity.this, "Enter your name first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (bio.isEmpty()) {
                Toast.makeText(EditActivity.this, "Enter Bio First", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, RequestBody> map = new HashMap<>();

            MultipartBody.Part body = null;

            if (picturePath != null && !picturePath.isEmpty()) {
                File file = new File(picturePath);
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("profileImage", file.getName(), requestFile);
            }

            if (pictureCoverPath != null && !pictureCoverPath.isEmpty()) {
                File file = new File(pictureCoverPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("coverImage", file.getName(), requestFile);
            }

            RequestBody bodyUserid = RequestBody.create(MediaType.parse("text/plain"), sessionManager.getUser().getId());
            RequestBody bodyName = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody bodyUserName = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody bodyBio = RequestBody.create(MediaType.parse("text/plain"), bio);

            map.put("name", bodyName);
            map.put("username", bodyUserName);
            map.put("bio", bodyBio);
            map.put("userId", bodyUserid);

            Call<UserRoot> call = RetrofitBuilder.create().updateUser(map, body);

            call.enqueue(new Callback<UserRoot>() {
                @Override
                public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                    if (response.code() == 200 && response.isSuccessful() && response.body().getUser() != null) {
                        Log.d("TAG", "onResponse: ");
                        sessionManager.saveUser(response.body().getUser());
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UserRoot> call, Throwable t) {
                    Log.d("TAG", "onFailure: " + t.getMessage());
                }
            });
        });
    }


    private void openEditSheet() {
        binding.lytimg.setOnClickListener(v -> choosePhoto());
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            selectedImage = data.getData();

            Log.d("TAG", "onActivityResult: " + selectedImage);

            Glide.with(this).load(selectedImage).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(false).into(binding.userImg);

            String[] filePathColumn = {DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            Log.d("TAG", "picpath:2 " + picturePath);

            //  sessionManager.saveStringValue(Const.IMAGEPATH, picturePath);

            Log.d("TAG", "onActivityResultpicpath: " + picturePath);
        }

        if (requestCode == GALLERY_COVER_CODE && resultCode == RESULT_OK && null != data) {
            binding.coverLay.setVisibility(View.GONE);

            selectedCoverImage = data.getData();
            Log.d("TAG", "onActivityResult: " + selectedCoverImage);
            Glide.with(this).load(selectedCoverImage).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(false).into(binding.coverImag);

            String[] filePathColumn = {DATA};

            Cursor cursor = getContentResolver().query(selectedCoverImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            pictureCoverPath = cursor.getString(columnIndex);
            cursor.close();

            Log.d("TAG", "picpath:2 " + pictureCoverPath);
            Log.d("TAG", "onActivityResultpicpath: " + pictureCoverPath);

        }

    }


    private void chooseCoverPhoto() {
        if (checkPermission()) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select picture using"), GALLERY_CODE);


            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.addCategory(Intent.CATEGORY_APP_GALLERY);
//            startActivityForResult(Intent.createChooser(intent, "Select picture using"), GALLERY_COVER_CODE);

//            Intent i = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(i, GALLERY_COVER_CODE);

        } else {
            requestPermission();
        }
    }

    private void choosePhoto() {

        if (checkPermission()) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select picture using"), GALLERY_CODE);

//            Intent i = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(i, GALLERY_CODE);

        } else {
            requestPermission();
        }

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


}