package com.video.buzzy.modelRetrofit;

import com.google.gson.annotations.SerializedName;

public class FacebookLoginRoot {

    @SerializedName("name")
    private String name;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("id")
    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("email")
    private String email;

    @SerializedName("picture")
    private Picture picture;

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public Picture getPicture() {
        return picture;
    }

    public static class Data {

        @SerializedName("is_silhouette")
        private boolean isSilhouette;

        @SerializedName("width")
        private int width;

        @SerializedName("url")
        private String url;

        @SerializedName("height")
        private int height;

        public boolean isIsSilhouette() {
            return isSilhouette;
        }

        public int getWidth() {
            return width;
        }

        public String getUrl() {
            return url;
        }

        public int getHeight() {
            return height;
        }
    }

    public static class Picture {

        @SerializedName("data")
        private Data data;

        public Data getData() {
            return data;
        }
    }
}