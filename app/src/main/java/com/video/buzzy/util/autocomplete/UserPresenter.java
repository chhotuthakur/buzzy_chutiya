package com.video.buzzy.util.autocomplete;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.otaliastudios.autocomplete.RecyclerViewPresenter;
import com.video.buzzy.modelRetrofit.SearchUserRoot;
import com.video.buzzy.retrofit.RetrofitBuilder;
import com.video.buzzy.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPresenter extends RecyclerViewPresenter<SearchUserRoot.SearchItem> {

    private static final String TAG = "UserPresenter";
    private final Context mContext;
    SessionManager sessionManager;
    private UserAdapter mAdapter;
    private Call<SearchUserRoot> call;
    // private Call<GuestUsersListRoot> call;

    public UserPresenter(Context context) {
        super(context);
        mContext = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    protected UserAdapter instantiateAdapter() {
        return mAdapter = new UserAdapter(mContext, new UserAdapter.OnClickListener() {
            @Override
            public void onUserClick(SearchUserRoot.SearchItem item) {
                UserPresenter.this.dispatchClick(item);
            }
        });
    }

    @Override
    protected void onQuery(@Nullable CharSequence q) {
        Log.v(TAG, "Querying '" + q + "' for users autocomplete.");
        //mAdapter.submitData(Demo_contents.getMentionSlim());


        if (call != null) {
            call.cancel();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", sessionManager.getUser().getId());
        jsonObject.addProperty("search", q.toString());
        call = RetrofitBuilder.create().searchUser(jsonObject);
        call.enqueue(new Callback<SearchUserRoot>() {
            @Override
            public void onResponse(Call<SearchUserRoot> call, Response<SearchUserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getSearch().isEmpty()) {
                        //mAdapter.clear();
                        mAdapter.submitData(response.body().getSearch());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchUserRoot> call, Throwable t) {

            }
        });


       /* REST rest = MainApplication.getContainer().get(REST.class);
        mCall = rest.usersIndex(q != null ? q.toString() : null, 1);
        mCall.enqueue(new Callback<Wrappers.Paginated<User>>() {

            @Override
            public void onResponse(
                    @Nullable Call<Wrappers.Paginated<User>> call,
                    @Nullable Response<Wrappers.Paginated<User>> response
            ) {
                Log.v(TAG, "Server responded with " + response.code() + " status.");
                if (response.isSuccessful()) {
                    Wrappers.Paginated<User> users = response.body();
                    mAdapter.submitData(users.data);
                }
            }

            @Override
            public void onFailure(
                    @Nullable Call<Wrappers.Paginated<User>> call,
                    @Nullable Throwable t
            ) {
                Log.e(TAG, "Fetching users has failed.", t);
            }
        });*/
    }
}
