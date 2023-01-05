package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

public class ChatTopic {

    @SerializedName("chatTopic")
    private ChatTopic chatTopic;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("senderUserId")
    private String senderUserId;

    @SerializedName("chat")
    private String chat;

    @SerializedName("_id")
    private String id;

    @SerializedName("receiverUserId")
    private String receiverUserId;

    @SerializedName("updatedAt")
    private String updatedAt;

    public ChatTopic getChatTopic() {
        return chatTopic;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public Object getChat() {
        return chat;
    }

    public String getId() {
        return id;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}