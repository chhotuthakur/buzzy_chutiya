package com.video.buzzy.design;

public class SuggestedUser {
    String name, email, image;
    boolean isfollowed;


    public SuggestedUser(String name, String email, String image, boolean isfollowed) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.isfollowed = isfollowed;
    }

    public SuggestedUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIsfollowed() {
        return isfollowed;
    }

    public void setIsfollowed(boolean isfollowed) {
        this.isfollowed = isfollowed;
    }


}
