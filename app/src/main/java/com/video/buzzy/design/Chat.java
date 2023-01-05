package com.video.buzzy.design;

public class Chat {
    public static int MAIN_USER = 1, USER = 2;
    String data, msgtye;
    int sender, img;

    public Chat(String data, String msgtye, int sender, int img) {
        this.data = data;
        this.msgtye = msgtye;
        this.sender = sender;
        this.img = img;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsgtye() {
        return msgtye;
    }

    public void setMsgtye(String msgtye) {
        this.msgtye = msgtye;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
