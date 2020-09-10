
package com.tetravalstartups.dingdong.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("following")
    @Expose
    private Integer following;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("videos")
    @Expose
    private Integer videos;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public Data() {
    }

    public Data(String id, String handle, String name, String email, Integer likes, Integer following, Integer followers, Integer videos, String bio, String photo, String timestamp) {
        this.id = id;
        this.handle = handle;
        this.name = name;
        this.email = email;
        this.likes = likes;
        this.following = following;
        this.followers = followers;
        this.videos = videos;
        this.bio = bio;
        this.photo = photo;
        this.timestamp = timestamp;
    }

    protected Data(Parcel in) {
        id = in.readString();
        handle = in.readString();
        name = in.readString();
        email = in.readString();
        if (in.readByte() == 0) {
            likes = null;
        } else {
            likes = in.readInt();
        }
        if (in.readByte() == 0) {
            following = null;
        } else {
            following = in.readInt();
        }
        if (in.readByte() == 0) {
            followers = null;
        } else {
            followers = in.readInt();
        }
        if (in.readByte() == 0) {
            videos = null;
        } else {
            videos = in.readInt();
        }
        bio = in.readString();
        photo = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getVideos() {
        return videos;
    }

    public void setVideos(Integer videos) {
        this.videos = videos;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(handle);
        parcel.writeString(name);
        parcel.writeString(email);
        if (likes == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(likes);
        }
        if (following == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(following);
        }
        if (followers == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(followers);
        }
        if (videos == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(videos);
        }
        parcel.writeString(bio);
        parcel.writeString(photo);
        parcel.writeString(timestamp);
    }
}
