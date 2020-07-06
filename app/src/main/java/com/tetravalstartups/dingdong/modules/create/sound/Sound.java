package com.tetravalstartups.dingdong.modules.create.sound;

import android.os.Parcel;
import android.os.Parcelable;

public class Sound implements Parcelable {

    private String id;
    private String cat_id;
    private String cat_name;
    private String media;
    private String name;
    private String artist;
    private String duration;
    private String banner;
    private String status;

    public Sound() {
    }

    public Sound(String id, String cat_id, String cat_name, String media, String name, String artist, String duration, String banner, String status) {
        this.id = id;
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.media = media;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.banner = banner;
        this.status = status;
    }

    protected Sound(Parcel in) {
        id = in.readString();
        cat_id = in.readString();
        cat_name = in.readString();
        media = in.readString();
        name = in.readString();
        artist = in.readString();
        duration = in.readString();
        banner = in.readString();
        status = in.readString();
    }

    public static final Creator<Sound> CREATOR = new Creator<Sound>() {
        @Override
        public Sound createFromParcel(Parcel in) {
            return new Sound(in);
        }

        @Override
        public Sound[] newArray(int size) {
            return new Sound[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cat_id);
        dest.writeString(cat_name);
        dest.writeString(media);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(duration);
        dest.writeString(banner);
        dest.writeString(status);
    }
}
