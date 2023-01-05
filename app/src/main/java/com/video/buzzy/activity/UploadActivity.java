package com.video.buzzy.activity;


import static android.provider.MediaStore.MediaColumns.DATA;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.video.buzzy.R;
import com.video.buzzy.database.ClientDatabase;
import com.video.buzzy.databinding.ActivityUploadBinding;
import com.video.buzzy.databinding.BottomSheetPrivacyBinding;
import com.video.buzzy.modelRetrofit.LocationRoot;
import com.video.buzzy.other.MainApplication;
import com.video.buzzy.socialView.SocialEditText;
import com.video.buzzy.util.AutocompleteUtil;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.util.SocialSpanUtil;
import com.video.buzzy.util.TempUtil;
import com.video.buzzy.util.VideoUtil;
import com.video.buzzy.workers.Draft;
import com.video.buzzy.workers.FixFastStartWorker2;
import com.video.buzzy.workers.GeneratePreviewWorker;
import com.video.buzzy.workers.UploadClipWorker;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.Disposable;


public class UploadActivity extends BaseActivity {

    public static final String EXTRA_DRAFT = "draft";
    public static final String EXTRA_SONG = "song";
    public static final String EXTRA_VIDEO = "video";
    private static final String TAG = "UploadActivity";
    private static final int GALLERY_COVER_CODE = 1002;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final int GALLERY_CODE = 1001;

    private final List<Disposable> mDisposables = new ArrayList<>();
    ActivityUploadBinding binding;
    boolean isshop = false;
    private boolean mDeleteOnExit = true;
    private Draft mDraft;
    private String mVideo;
    private UploadActivityViewModel mModel;
    private String mSong;
    private SessionManager sessionManager;
    private Privacy privacy;
    private LocationRoot.ResponseItem selectedLocation;
    private Uri selectedImage;
    private String picturePath = "";
    boolean isShopeView = false;
//    private SearchLocationRoot.ResponseItem selectedLocation;
//    private RayziUtils.Privacy privacy = RayziUtils.Privacy.PUBLIC;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LocationChooseActivity.REQ_CODE_LOCATION && resultCode == RESULT_OK && data != null) {
            String locationData = data.getStringExtra(Const.DATA);
            LocationRoot.ResponseItem location = new Gson().fromJson(locationData, LocationRoot.ResponseItem.class);
            if (location != null) {
                selectedLocation = location;
                if (selectedLocation != null) {
                    binding.tvLocation.setText(location.getCity() + ", " + location.getState() + "," + location.getCountryName());
                    mModel.location = binding.tvLocation.getText().toString();
                }
            }
        }
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


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload);
        mDraft = getIntent().getParcelableExtra(EXTRA_DRAFT);

        sessionManager = new SessionManager(this);

        mModel = new ViewModelProvider(this).get(UploadActivityViewModel.class);

        if (mDraft != null) {
            mSong = !mDraft.songId.isEmpty() ? mDraft.songId : "";
            mVideo = mDraft.video;
            mModel.preview = mDraft.preview;
            mModel.screenshot = mDraft.screenshot;
            mModel.description = mDraft.description;
            mModel.privacy = mDraft.privacy;
            mModel.hasComments = mDraft.hasComments;
            mModel.location = mDraft.location;
            binding.decriptionView.setText(mDraft.description);
            binding.tvLocation.setText(mDraft.location);
            binding.switchComments.setChecked(mDraft.hasComments);

        } else {
            mSong = getIntent().getStringExtra(EXTRA_SONG);
            mVideo = getIntent().getStringExtra(EXTRA_VIDEO);
            Log.d(TAG, "onCreate:songid " + mSong);
        }

        Bitmap image = VideoUtil.getFrameAtTime(mVideo, TimeUnit.SECONDS.toMicros(3));
        ImageView thumbnail = findViewById(R.id.imageview);
        thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        thumbnail.setImageBitmap(image);


        thumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra(PreviewActivity.EXTRA_VIDEO, mVideo);
            startActivity(intent);
        });

        binding.back.setOnClickListener(v -> {
            finish();
        });


        binding.lytLocation.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, LocationChooseActivity.class).putExtra(Const.DATA, binding.tvLocation.getText().toString()), LocationChooseActivity.REQ_CODE_LOCATION);
        });

        binding.switchComments.setOnCheckedChangeListener((buttonView, isChecked) -> mModel.hasComments = isChecked);

        binding.switchShop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                binding.shopLay.setVisibility(View.VISIBLE);
            else binding.shopLay.setVisibility(View.GONE);

        });

        binding.lytimg.setOnClickListener(v -> {
            choosePhoto();
        });

        binding.lytPrivacy.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.customStyle);
            BottomSheetPrivacyBinding sheetPrivacyBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottom_sheet_privacy, null, false);
            bottomSheetDialog.setContentView(sheetPrivacyBinding.getRoot());
            bottomSheetDialog.show();
            sheetPrivacyBinding.tvPublic.setOnClickListener(v1 -> {
                setPrivacy(Privacy.PUBLIC);
                bottomSheetDialog.dismiss();
            });
            sheetPrivacyBinding.tvOnlyFollowr.setOnClickListener(v1 -> {
                setPrivacy(Privacy.FOLLOWRS);
                bottomSheetDialog.dismiss();
            });

        });


        SocialEditText description = findViewById(R.id.decriptionView);
        description.setText(mModel.description);
        @NonNull Disposable disposable = RxTextView.afterTextChangeEvents(binding.decriptionView)
                .skipInitialValue()
                .subscribe(e -> {
                    Editable editable = e.getEditable();
                    mModel.description = editable != null ? editable.toString() : null;
                });
        mDisposables.add(disposable);

        SocialSpanUtil.apply(binding.decriptionView, mModel.description, null);
        AutocompleteUtil.setupForHashtags(this, binding.decriptionView);
        AutocompleteUtil.setupForUsers(this, binding.decriptionView);

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

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private int getPrivacy() {
        if (privacy == Privacy.FOLLOWRS) {
            return 1;
        } else {
            return 0;
        }
    }

    private void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
        if (privacy == Privacy.FOLLOWRS) {
            binding.tvPrivacy.setText("My Followers");
        } else {
            binding.tvPrivacy.setText("Public");
        }
    }

    private void uploadToServer() {
        WorkManager wm = WorkManager.getInstance(this);
        OneTimeWorkRequest request;

        binding.decriptionView.setText(binding.decriptionView.getText().toString());

        String hashtag = "", mentionPeoples = "";

        for (int i = 0; i < binding.decriptionView.getHashtags().size(); i++) {
            Log.d(TAG, "onClickPost: hash  " + binding.decriptionView.getHashtags().get(i));
            hashtag = hashtag + binding.decriptionView.getHashtags().get(i) + ",";
        }
        for (int i = 0; i < binding.decriptionView.getMentions().size(); i++) {
            Log.d(TAG, "onClickPost: mens  " + binding.decriptionView.getMentions().get(i));
            mentionPeoples = mentionPeoples + binding.decriptionView.getMentions().get(i) + ",";
            Log.d(TAG, "onClickPost: mens2  " + mentionPeoples);
        }

//        if(hashtag.equals(""))
//        {
//            hashtag= "";
//        }
//
//        if(mentionPeoples.equals(""))
//        {
//            mentionPeoples = "";
//        }


        String url = binding.etProductUrl.getText().toString();


        if (mDraft != null) {

            LocalVideo localVideo = new LocalVideo(mSong,
                    mDraft.video, mDraft.screenshot, mDraft.preview,
                    mModel.description, mModel.location, sessionManager.getUser().getId(),
                    hashtag, mentionPeoples,
                    mModel.hasComments, getPrivacy(), picturePath, url, "Shope Now");

            sessionManager.saveLocalVideo(localVideo);

            request = new OneTimeWorkRequest.Builder(UploadClipWorker.class)
                    .build();
            wm.enqueue(request);
            ClientDatabase db = MainApplication.getContainer().get(ClientDatabase.class);
            db.drafts().delete(mDraft);
        } else {

            File video = TempUtil.createNewFile(getFilesDir(), ".mp4");
            File preview = TempUtil.createNewFile(getFilesDir(), ".gif");
            // File preview = TempUtil.createNewFile(getFilesDir(), ".png");
            File screenshot = TempUtil.createNewFile(getFilesDir(), ".png");
            Data data1 = new Data.Builder()
                    .putString(FixFastStartWorker2.KEY_INPUT, mVideo)
                    .putString(FixFastStartWorker2.KEY_OUTPUT, video.getAbsolutePath())
                    .build();
            OneTimeWorkRequest request1 = new OneTimeWorkRequest.Builder(FixFastStartWorker2.class)
                    .setInputData(data1)
                    .build();
            Data data2 = new Data.Builder()
                    .putString(GeneratePreviewWorker.KEY_INPUT, video.getAbsolutePath())
                    .putString(GeneratePreviewWorker.KEY_SCREENSHOT, screenshot.getAbsolutePath())
                    .putString(GeneratePreviewWorker.KEY_PREVIEW, preview.getAbsolutePath())
                    .build();
            OneTimeWorkRequest request2 = new OneTimeWorkRequest.Builder(GeneratePreviewWorker.class)
                    .setInputData(data2)
                    .build();


/*
            Data mydata = new Data.Builder()
                    .putString(UploadClipWorker.KEY_SONG, mSong)
                    .putString(UploadClipWorker.KEY_VIDEO, video.getAbsolutePath())
                    .putString(UploadClipWorker.KEY_SCREENSHOT, screenshot.getAbsolutePath())
                    .putString(UploadClipWorker.KEY_PREVIEW, preview.getAbsolutePath())
                    .putString(UploadClipWorker.KEY_DESCRIPTION, mModel.description)
                    .putString(UploadClipWorker.KEY_HESHTAGS,new Gson().toJson(hestags))
                    .putString(UploadClipWorker.KEY_MENTIONS,new Gson().toJson(mentionPeoples))
                    .putBoolean(UploadClipWorker.KEY_COMMENTS, mModel.hasComments)
                    .putString(UploadClipWorker.KEY_LOCATION, mModel.location)
                    .putInt(UploadClipWorker.KEY_PRIVACY, 0)
                    .putString(UploadClipWorker.KEY_USERID, sessionManager.getUser().getId())
                    .build();
*/

            LocalVideo localVideo = new LocalVideo(mSong,
                    video.getAbsolutePath(), screenshot.getAbsolutePath(), preview.getAbsolutePath(),
                    mModel.description, mModel.location, sessionManager.getUser().getId(),
                    hashtag, mentionPeoples,
                    mModel.hasComments, getPrivacy(), picturePath, url, "Shope Now");
            sessionManager.saveLocalVideo(localVideo);
            Data data3 = new Data.Builder()
                    .build();
            request = new OneTimeWorkRequest.Builder(UploadClipWorker.class)
                    .setInputData(data3)
                    .build();
            wm.beginWith(request1)
                    .then(request2)
                    .then(request)
                    .enqueue();
        }

        if (getResources().getBoolean(R.bool.uploads_async_enabled)) {
            mDeleteOnExit = false;
            Toast.makeText(this, R.string.uploading_message, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            KProgressHUD progress = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(getString(R.string.progress_title))
                    .setCancellable(false)
                    .show();
            wm.getWorkInfoByIdLiveData(request.getId())
                    .observe(this, info -> {
                        boolean ended = info.getState() == WorkInfo.State.CANCELLED
                                || info.getState() == WorkInfo.State.FAILED
                                || info.getState() == WorkInfo.State.SUCCEEDED;
                        if (ended) {
                            progress.dismiss();
                        }

                        if (info.getState() == WorkInfo.State.SUCCEEDED) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Disposable disposable : mDisposables) {
            disposable.dispose();
        }

        mDisposables.clear();
        if (mDeleteOnExit && mDraft == null) {
            File video = new File(mVideo);
            if (!video.delete()) {
                Log.w(TAG, "Could not delete input video: " + video);
            }
        }
    }

    private void deleteDraft() {
        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.confirmation_delete_draft)
                .setNegativeButton(R.string.cancel_button, (dialog, i) -> dialog.cancel())
                .setPositiveButton("Yes", (dialog, i) -> {
                    dialog.dismiss();
                    FileUtils.deleteQuietly(new File(mDraft.preview));
                    FileUtils.deleteQuietly(new File(mDraft.screenshot));
                    FileUtils.deleteQuietly(new File(mDraft.video));
                    ClientDatabase db = MainApplication.getContainer().get(ClientDatabase.class);
                    db.drafts().delete(mDraft);
                    setResult(RESULT_OK);
                    finish();
                })
                .show();
    }

    public void onClickPost(View view) {
        String s = binding.decriptionView.getText().toString();
        if (selectedLocation == null) {
            mModel.location = "";
        }

//        if (binding.switchShop.isChecked()) {
//            if (picturePath == null) {
//                Toast.makeText(UploadActivity.this, "Please enter product image", Toast.LENGTH_SHORT).show();
//                return;
//            } else if (binding.etProductUrl.getText().toString().equals("")) {
//                Toast.makeText(UploadActivity.this, "Please enter product url", Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                isShopeView = true;
//            }
//        }

        uploadToServer();
    }


    public enum Privacy {
        PUBLIC, FOLLOWRS, PRIVATE
    }

    public static class UploadActivityViewModel extends ViewModel {
        public String description = null;
        public boolean hasComments = true;
        public String location = "";
        public String preview;
        public String screenshot;
        public int privacy;
        public String[] heshtags;
        public String[] mentions;
    }

    public class LocalVideo {
        String songId, video, screenshot, preview, decritption, location, userId, productimage, producturl, producttag;
        String heshtags;
        String mentions;
        boolean hasComments, isOriginalS;
        int privacy;

        public LocalVideo(String songId, String video, String screenshot,
                          String preview, String decritption, String location,
                          String userId, String heshtags, String mentions,
                          boolean hasComments, int privacy, String productimage, String producturl, String producttag) {
            this.songId = songId;
            this.video = video;
            this.screenshot = screenshot;
            this.preview = preview;
            this.decritption = decritption;
            this.location = location;
            this.userId = userId;
            this.productimage = productimage;
            this.producturl = producturl;
            this.producttag = producttag;
            this.heshtags = heshtags;
            this.mentions = mentions;
            this.hasComments = hasComments;
            this.privacy = privacy;
        }

        public String getProductimage() {
            return productimage;
        }

        public void setProductimage(String productimage) {
            this.productimage = productimage;
        }

        public String getProducturl() {
            return producturl;
        }

        public void setProducturl(String producturl) {
            this.producturl = producturl;
        }

        public String getProducttag() {
            return producttag;
        }

        public void setProducttag(String producttag) {
            this.producttag = producttag;
        }

        public String getSongId() {
            return songId;
        }

        public void setSongId(String songId) {
            this.songId = songId;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

        public String getDecritption() {
            return decritption;
        }

        public void setDecritption(String decritption) {
            this.decritption = decritption;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getHeshtags() {
            return heshtags;
        }

        public void setHeshtags(String heshtags) {
            this.heshtags = heshtags;
        }

        public String getMentions() {
            return mentions;
        }

        public void setMentions(String mentions) {
            this.mentions = mentions;
        }

        public boolean isHasComments() {
            return hasComments;
        }

        public void setHasComments(boolean hasComments) {
            this.hasComments = hasComments;
        }

        public int getPrivacy() {
            return privacy;
        }

        public void setPrivacy(int privacy) {
            this.privacy = privacy;
        }
    }
}
