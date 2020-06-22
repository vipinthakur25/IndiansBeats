package com.tetravalstartups.dingdong.modules.profile.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InProfileCreatedVideo implements Parcelable {
    private String id;
    private String views;
    private String thumbnail;

    public InProfileCreatedVideo() {
    }

    public InProfileCreatedVideo(String id, String views, String thumbnail) {
        this.id = id;
        this.views = views;
        this.thumbnail = thumbnail;
    }

    protected InProfileCreatedVideo(Parcel in) {
        id = in.readString();
        views = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<InProfileCreatedVideo> CREATOR = new Creator<InProfileCreatedVideo>() {
        @Override
        public InProfileCreatedVideo createFromParcel(Parcel in) {
            return new InProfileCreatedVideo(in);
        }

        @Override
        public InProfileCreatedVideo[] newArray(int size) {
            return new InProfileCreatedVideo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(views);
        dest.writeString(thumbnail);
    }
}
