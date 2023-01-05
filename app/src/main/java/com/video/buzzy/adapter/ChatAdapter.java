package com.video.buzzy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.video.buzzy.BuildConfig;
import com.video.buzzy.R;
import com.video.buzzy.activity.ImageActivity;
import com.video.buzzy.databinding.ItemChatMainUserBinding;
import com.video.buzzy.databinding.ItemChatUserBinding;
import com.video.buzzy.modelRetrofit.ChatRoot;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MAIN_USER_TYPE = 1;
    Context context;
    Bitmap bitmap;
    Intent intent;
    boolean issingle = false;
    String strchar;
    SessionManager sessionManager;
    private int USER_TYPE = 2;
    private String authorimage;
    private List<ChatRoot.ChatItem> chatlist = new ArrayList<>();

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainUserChatViewHolder) {
            Log.d("TAG", "onBindViewHolder: admin");
            ((MainUserChatViewHolder) holder).setdata(position);

        } else {
            Log.d("TAG", "onBindViewHolder: user");
            ((UserChatViewHolder) holder).setdata(position);
        }
    }

    @Override
    public int getItemViewType(int position) {

//        Log.d(">>>>>>>>", "getItemViewType: " + chatlist.get(position).getSenderId());

        if (chatlist.get(position).getSenderId().equals(Const.LOGINUSERID)) {
            Log.d("TAG", "getItemViewType: if");
            return MAIN_USER_TYPE;
        } else {
            Log.d("TAG", "getItemViewType: else ");
            return USER_TYPE;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManager = new SessionManager(context);

        if (viewType == MAIN_USER_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_main_user, parent, false);
            return new MainUserChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);
            return new UserChatViewHolder(view);
        }

    }


    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    public void addData(List<ChatRoot.ChatItem> data) {
        this.chatlist.addAll(data);
        notifyItemRangeInserted(this.chatlist.size(), data.size());
    }

    public void addSingleMessage(ChatRoot.ChatItem dataItem) {
        Log.d(">>>>>>>>", "addSingleMessage: " + dataItem.toString());
        chatlist.add(0, dataItem);
        notifyItemInserted(0);

    }

    public void setRecieverText(String str) {
        this.strchar = str;
    }

    public class MainUserChatViewHolder extends RecyclerView.ViewHolder {
        ItemChatMainUserBinding mainuserbinding;

        public MainUserChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mainuserbinding = ItemChatMainUserBinding.bind(itemView);
        }

        public void setdata(int position) {
            Glide.with(context).load(sessionManager.getUser().getProfileImage()).circleCrop().placeholder(R.drawable.shape_round__dark_blue).into(mainuserbinding.mainUserImg);

            ChatRoot.ChatItem chatItem = chatlist.get(position);


            if (chatItem.getMessageType() == 1) {
                mainuserbinding.imageLay.setVisibility(View.VISIBLE);
                mainuserbinding.TextLay.setVisibility(View.GONE);
                mainuserbinding.giftLay.setVisibility(View.GONE);
                mainuserbinding.loaderLay.setVisibility(View.VISIBLE);

                Glide.with(context).load(BuildConfig.BASE_URL + chatItem.getImage()).placeholder(R.drawable.placeholder_feed).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainuserbinding.loaderLay.setVisibility(View.GONE);
                        return false;
                    }
                }).into(mainuserbinding.image);


                mainuserbinding.imageLay.setOnClickListener(v -> {
                    Intent i = new Intent(context, ImageActivity.class);
                    i.putExtra(Const.IMAGEPATH, BuildConfig.BASE_URL + chatItem.getImage());
                    context.startActivity(i);
                });

            } else if (chatItem.getMessageType() == 0) {
                mainuserbinding.imageLay.setVisibility(View.GONE);
                mainuserbinding.TextLay.setVisibility(View.VISIBLE);
                mainuserbinding.giftLay.setVisibility(View.GONE);
                mainuserbinding.chatMsg.setText(chatItem.getMessage());
            } else if (chatItem.getMessageType() == 2) {
                mainuserbinding.imageLay.setVisibility(View.GONE);
                mainuserbinding.TextLay.setVisibility(View.GONE);
                mainuserbinding.giftLay.setVisibility(View.VISIBLE);
                mainuserbinding.loaderLay.setVisibility(View.VISIBLE);


                Glide.with(context).load(BuildConfig.BASE_URL + chatItem.getImage()).placeholder(R.drawable.placeholder_feed).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainuserbinding.loaderLay.setVisibility(View.GONE);
                        return false;
                    }
                }).into(mainuserbinding.giftImg);


                // Glide.with(context).load(BuildConfig.BASE_URL + chatItem.getImage()).into(mainuserbinding.giftImg);
            }

            mainuserbinding.time.setText(chatItem.getTime());
        }

    }

    public class UserChatViewHolder extends RecyclerView.ViewHolder {
        ItemChatUserBinding userbinding;

        public UserChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userbinding = ItemChatUserBinding.bind(itemView);
        }

        public void setdata(int position) {
//            userbinding.chatMsg.setText(chatlist.get(position).getMessage());
            userbinding.imgUser.setText(strchar);

            ChatRoot.ChatItem chatItem = chatlist.get(position);


            if (chatItem.getMessageType() == 1) {
                userbinding.imageLay.setVisibility(View.VISIBLE);
                userbinding.TextLay.setVisibility(View.GONE);
                userbinding.giftLay.setVisibility(View.GONE);
                Glide.with(context).load(BuildConfig.BASE_URL + chatItem.getImage()).into(userbinding.image);
            } else if (chatItem.getMessageType() == 0) {
                userbinding.imageLay.setVisibility(View.GONE);
                userbinding.TextLay.setVisibility(View.VISIBLE);
                userbinding.giftLay.setVisibility(View.GONE);
                userbinding.chatMsg.setText(chatItem.getMessage());
            } else if (chatItem.getMessageType() == 2) {
                userbinding.imageLay.setVisibility(View.GONE);
                userbinding.TextLay.setVisibility(View.GONE);
                userbinding.giftLay.setVisibility(View.VISIBLE);
                Glide.with(context).load(BuildConfig.BASE_URL + chatItem.getImage()).into(userbinding.giftImg);
            }

            String currentString = chatlist.get(position).getDate();
            String[] separated = currentString.split(",");
            String date = separated[1];

            userbinding.date.setText(chatItem.getTime());

        }
    }

}
