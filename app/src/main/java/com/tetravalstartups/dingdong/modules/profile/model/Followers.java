package com.tetravalstartups.dingdong.modules.profile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Followers implements Parcelable {
    @ServerTimestamp
    private Date timestamp;
    private String id;
    private String name;
    private String handle;
    private String photo;
    private boolean following;

    public Followers() {
    }

    public Followers(Date timestamp, String id, String name, String handle, String photo, boolean following) {
        this.timestamp = timestamp;
        this.id = id;
        this.name = name;
        this.handle = handle;
        this.photo = photo;
        this.following = following;
    }

    protected Followers(Parcel in) {
        id = in.readString();
        name = in.readString();
        handle = in.readString();
        photo = in.readString();
        following = in.readByte() != 0;
    }

    public static final Creator<Followers> CREATOR = new Creator<Followers>() {
        @Override
        public Followers createFromParcel(Parcel in) {
            return new Followers(in);
        }

        @Override
        public Followers[] newArray(int size) {
            return new Followers[size];
        }
    };

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(handle);
        dest.writeString(photo);
        dest.writeByte((byte) (following ? 1 : 0));
    }
}
