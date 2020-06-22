package com.tetravalstartups.dingdong.modules.create.sound;

import android.os.Parcel;
import android.os.Parcelable;

public class Music implements Parcelable {

    private String id;
    private String cat_id;
    private String cat_name;
    private String media;
    private String banner;
    private String name;
    private String artist;
    private String duration;
    private boolean favorite;
    private int status;

    public Music() {
    }

    public Music(String id, String cat_id, String cat_name, String media, String banner, String name, String artist, String duration, boolean favorite, int status) {
        this.id = id;
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.media = media;
        this.banner = banner;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.favorite = favorite;
        this.status = status;
    }

    protected Music(Parcel in) {
        id = in.readString();
        cat_id = in.readString();
        cat_name = in.readString();
        media = in.readString();
        banner = in.readString();
        name = in.readString();
        artist = in.readString();
        duration = in.readString();
        favorite = in.readByte() != 0;
        status = in.readInt();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
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

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
        dest.writeString(banner);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(duration);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeInt(status);
    }
}
