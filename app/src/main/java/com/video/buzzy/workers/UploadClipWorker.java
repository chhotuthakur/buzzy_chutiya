package com.video.buzzy.workers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.video.buzzy.R;
import com.video.buzzy.activity.UploadActivity;
import com.video.buzzy.database.ClientDatabase;
import com.video.buzzy.modelRetrofit.ReelsVideoRoot;
import com.video.buzzy.other.MainApplication;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;
import com.video.buzzy.util.VideoUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadClipWorker extends Worker {


    private static final String TAG = "UploadClipWorker";
    SessionManager sessionManager;
    String channelId = "";
    Notification notification;
    private Context context;

    public UploadClipWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    private ForegroundInfo createForegroundInfo(Context context) {
        String cancel = context.getString(R.string.cancel_button);
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("default", "My Background Service");
        }

        notification = new NotificationCompat.Builder(
                context, context.getString(R.string.notification_channel_id))
                .setContentTitle(context.getString(R.string.notification_upload_title))
                .setTicker(context.getString(R.string.notification_upload_title))
                .setContentText(context.getString(R.string.notification_upload_description))
                .setSmallIcon(R.drawable.ic_baseline_publish_24)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_baseline_close_24, cancel, intent)
                .build();

        return new ForegroundInfo(Const.NOTIFICATION_UPLOAD, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);
        return channelId;
    }

    @NonNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Result doWork() {
        setForegroundAsync(createForegroundInfo(getApplicationContext()));

        boolean success = false;
        UploadActivity.LocalVideo relite = sessionManager.getLocalVideo();
        if (relite != null) {
            String songId = relite.getSongId() != null ? relite.getSongId() : "";
            File video = new File(relite.getVideo());

            File screenshot = new File(relite.getScreenshot());
            File preview = new File(relite.getPreview());
            String description = relite.getDecritption();

            boolean hasComments = relite.isHasComments();
            String location = relite.getLocation();

            int privacy = relite.getPrivacy();
            String userId = relite.getUserId();


            String producturl = relite.getProducturl();
            String producttag = relite.getProducttag();

            long duration = VideoUtil.getDuration(getApplicationContext(), Uri.fromFile(video));
            duration = TimeUnit.MILLISECONDS.toSeconds(duration);


            try {
                Log.d(TAG, "doWork: file upload work-------------------------------");
                Log.d(TAG, "doWork: video: " + video + "\nss: " + screenshot + "\npreview: " + preview + "\n songid: " + songId + "\ndec: " + description + "\ndur: " + duration + "\nhascmt: " + hasComments + " location: " + location + " \nuid:" + userId);
                success = doActualWork(
                        video, screenshot, preview, songId, description, (int) duration,
                        hasComments, location, userId, privacy, relite.getMentions(), relite.getHeshtags(), relite.getProductimage(), producturl, producttag);
            } catch (Exception e) {
                Log.e(TAG, "Failed to upload clip to server.", e);
            }

            if (success && !video.delete()) {
                Log.w(TAG, "Could not delete uploaded Yvideo file.");
            }

            if (success && !screenshot.delete()) {
                Log.w(TAG, "Could not delete uploaded screenshot file.");
            }

            if (success && !preview.delete()) {
                Log.w(TAG, "Could not delete uploaded preview file.");
            }

            if (!success) {
                Draft draft = createDraft(
                        video, screenshot, preview, songId, description, relite.getHeshtags(), relite.getMentions(), privacy,
                        hasComments, location);
                Log.w(TAG, "Failed clip saved as draft with ID " + draft.id + ".");
                EventBus.getDefault().post(new ResetDraftsEvent());
                createDraftNotification(draft);
            }
            return success ? Result.success() : Result.failure();
        }
        Log.e(TAG, "doWork: uploaded success");
        return success ? Result.success() : Result.failure();
    }


    private Draft createDraft(
            File video,
            File screenshot,
            File preview,
            String songId,
            String description,
            String heshtags,
            String mentions,
            int privacy,
            boolean hasComments,
            String location) {

        ClientDatabase db = MainApplication.getContainer().get(ClientDatabase.class);
        Draft draft = new Draft();
        draft.video = video.getAbsolutePath();
        draft.screenshot = screenshot.getAbsolutePath();
        draft.preview = preview.getAbsolutePath();
        draft.songId = songId;
        draft.description = description;

        draft.privacy = privacy;
        draft.hasComments = hasComments;
        draft.location = location;

        db.drafts().insert(draft);
        return draft;
    }

    private void createDraftNotification(Draft draft) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, UploadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(UploadActivity.EXTRA_DRAFT, draft);
        PendingIntent pi = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification =
                new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .setContentText(context.getString(R.string.notification_upload_failed_description))
                        .setContentTitle(context.getString(R.string.notification_upload_failed_title))
                        .setSmallIcon(R.drawable.ic_baseline_redo_24)
                        .setTicker(context.getString(R.string.notification_upload_failed_title))
                        .build();
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(Const.NOTIFICATION_UPLOAD_FAILED, notification);
    }


    private boolean doActualWork(
            File video,
            File screenshot,
            File preview,
            String songId,
            String description,
            int duration,
            boolean hasComments,
            String location,
            String userId,
            int privacy,
            String mentions,
            String heshtags,
            String productimage,
            String producturl,
            String producttag) {


        MultipartBody.Part body1 = null;
        if (video != null && !video.getPath().isEmpty()) {
            File file = new File(video.getAbsolutePath());

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body1 = MultipartBody.Part.createFormData("video", file.getName(), requestFile);

        }

        MultipartBody.Part body2 = null;
        if (screenshot != null && !screenshot.getPath().isEmpty()) {
            File file = new File(screenshot.getAbsolutePath());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body2 = MultipartBody.Part.createFormData("screenshot", file.getName(), requestFile);

        }

        MultipartBody.Part body3 = null;
        if (preview != null && !preview.getPath().isEmpty()) {
            File file = new File(preview.getAbsolutePath());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body3 = MultipartBody.Part.createFormData("thumbnail", file.getName(), requestFile);

        }

        MultipartBody.Part body4 = null;
        if (productimage != null && !productimage.isEmpty()) {
            File file = new File(productimage);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body4 = MultipartBody.Part.createFormData("productImage", file.getName(), requestFile);

        }

        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("userId", RequestBody.create(MediaType.parse("text/plain"), userId));

        if (songId.isEmpty()) {
            hashMap.put("isOriginalAudio", RequestBody.create(MediaType.parse("text/plain"), "true"));
        } else {
            hashMap.put("isOriginalAudio", RequestBody.create(MediaType.parse("text/plain"), "false"));
            hashMap.put("songId", RequestBody.create(MediaType.parse("text/plain"), songId));
        }

        Log.d(">>>>>>>", "doActualWork:  mention" + mentions);
        Log.d(">>>>>>>", "doActualWork: hashrag " + heshtags);

        hashMap.put("allowComment", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(hasComments)));
        hashMap.put("caption", RequestBody.create(MediaType.parse("text/plain"), description));
        hashMap.put("showVideo", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(privacy)));
        hashMap.put("location", RequestBody.create(MediaType.parse("text/plain"), location));
        hashMap.put("hashtag", RequestBody.create(MediaType.parse("text/plain"), heshtags));
        hashMap.put("mentionPeople", RequestBody.create(MediaType.parse("text/plain"), mentions));
        hashMap.put("duration", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(duration)));
        hashMap.put("size", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(VideoUtil.getFileSizeInMB(video) + " MB")));
        hashMap.put("productUrl", RequestBody.create(MediaType.parse("text/plain"), producturl));
        hashMap.put("productTag", RequestBody.create(MediaType.parse("text/plain"), producttag));

        Log.d(TAG, "doActualWork: size " + VideoUtil.getFileSizeInMB(video));
        Log.d(TAG, "doActualWork: duration " + duration);

        final boolean[] success = {false};

        Call<ReelsVideoRoot> call = RetrofitBuilder.create().uploadReels(hashMap, body1, body2, body3, body4);

        call.enqueue(new Callback<ReelsVideoRoot>() {
            @Override
            public void onResponse(Call<ReelsVideoRoot> call, Response<ReelsVideoRoot> response) {
                if (response.code() == 200 && response.isSuccessful() && response.body().getReel() != null) {
                    Log.d(TAG, "onResponse: ");
                    Toast.makeText(getApplicationContext(), "Uploaded Successfully..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReelsVideoRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


//        Response<Rel> response = null;
//        try {
//            response = call.execute();
//        } catch (Exception e) {
//            Log.e(TAG, "Failed when uploading clip to server.", e);
//        }
//
//        if (response != null) {
//            if (response.code() == 422) {
//                try {
//                    Log.v(TAG, "Server returned validation errors:\n" + response.errorBody().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            return response.isSuccessful();
//        }

       /* call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                Log.d(TAG, "onResponse: success");
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        success[0] = true;
                        uploadClipLister.onSuccess();
                    } else {
                        uploadClipLister.onFailure();
                        success[0] = false;
                    }
                } else {
                    uploadClipLister.onFailure();
                    success[0] = false;
                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: err  " + t.getMessage());
                success[0] = false;
            }
        });
*/

        Log.d(TAG, "doActualWork: done ");

        return success[0];
    }

    public interface UploadClipLister {
        void onSuccess();

        void onFailure();
    }


    private class ResetDraftsEvent {
    }
}
