package com.video.buzzy.util.autocomplete;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HashtagPresenter extends RecyclerViewPresenter<HashtagVideoRoot.HashtagItem> {

    private static final String TAG = "HashtagPresenter";
    private final Context mContext;
    private HashtagAdapter mAdapter;
    private Call<HashtagVideoRoot> hashtagroot;
    private int start = 0;
    // private Call<HeshtagsRoot> hashtagroot;

    public HashtagPresenter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected HashtagAdapter instantiateAdapter() {
        return mAdapter = new HashtagAdapter(mContext, item -> {
            dispatchClick(item);
        });
    }

    @Override
    protected void onQuery(@Nullable CharSequence q) {
        Log.v(TAG, "Querying '" + q + "' for hashtags autocomplete.");
        // mAdapter.submitData(Demo_contents.getHashtagsAuto());

        if (hashtagroot != null) {
            hashtagroot.cancel();
        }

        hashtagroot = RetrofitBuilder.create().getHashtagVideo(q.toString(), start, 50);
        hashtagroot.enqueue(new Callback<HashtagVideoRoot>() {
            @Override
            public void onResponse(Call<HashtagVideoRoot> call, Response<HashtagVideoRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getHashtag().isEmpty()) {
                        mAdapter.submitData(response.body().getHashtag());
                    }
                }
            }

            @Override
            public void onFailure(Call<HashtagVideoRoot> call, Throwable t) {

            }
        });

       /* REST rest = MainApplication.getContainer().get(REST.class);
        mCall = rest.hashtagsIndex(q != null ? q.toString() : null, 1);
        mCall.enqueue(new Callback<Wrappers.Paginated<Hashtag>>() {

            @Override
            public void onResponse(
                    @Nullable Call<Wrappers.Paginated<Hashtag>> call,
                    @Nullable Response<Wrappers.Paginated<Hashtag>> response
            ) {
                Log.v(TAG, "Server responded with " + response.code() + " status.");
                if (response.isSuccessful()) {
                    Wrappers.Paginated<Hashtag> hashtags = response.body();
                    mAdapter.submitData(hashtags.data);
                }
            }

            @Override
            public void onFailure(
                    @Nullable Call<Wrappers.Paginated<Hashtag>> call,
                    @Nullable Throwable t
            ) {
                Log.e(TAG, "Fetching hashtags has failed.", t);
            }
        });*/
    }
}
