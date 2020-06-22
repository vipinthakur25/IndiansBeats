package com.tetravalstartups.dingdong.modules.home.video;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
    private String video_id;
    private String video_url;
    private String video_user_id;
    private String video_user_name;
    private String video_user_photo;
    private String video_user_followers;
    private String video_likes;
    private String video_comments;
    private String video_shares;

    public Video(String video_id, String video_url, String video_user_id, String video_user_name, String video_user_photo, String video_user_followers, String video_likes, String video_comments, String video_shares) {
        this.video_id = video_id;
        this.video_url = video_url;
        this.video_user_id = video_user_id;
        this.video_user_name = video_user_name;
        this.video_user_photo = video_user_photo;
        this.video_user_followers = video_user_followers;
        this.video_likes = video_likes;
        this.video_comments = video_comments;
        this.video_shares = video_shares;
    }

    protected Video(Parcel in) {
        video_id = in.readString();
        video_url = in.readString();
        video_user_id = in.readString();
        video_user_name = in.readString();
        video_user_photo = in.readString();
        video_user_followers = in.readString();
        video_likes = in.readString();
        video_comments = in.readString();
        video_shares = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_user_id() {
        return video_user_id;
    }

    public void setVideo_user_id(String video_user_id) {
        this.video_user_id = video_user_id;
    }

    public String getVideo_user_name() {
        return video_user_name;
    }

    public void setVideo_user_name(String video_user_name) {
        this.video_user_name = video_user_name;
    }

    public String getVideo_user_photo() {
        return video_user_photo;
    }

    public void setVideo_user_photo(String video_user_photo) {
        this.video_user_photo = video_user_photo;
    }

    public String getVideo_user_followers() {
        return video_user_followers;
    }

    public void setVideo_user_followers(String video_user_followers) {
        this.video_user_followers = video_user_followers;
    }

    public String getVideo_likes() {
        return video_likes;
    }

    public void setVideo_likes(String video_likes) {
        this.video_likes = video_likes;
    }

    public String getVideo_comments() {
        return video_comments;
    }

    public void setVideo_comments(String video_comments) {
        this.video_comments = video_comments;
    }

    public String getVideo_shares() {
        return video_shares;
    }

    public void setVideo_shares(String video_shares) {
        this.video_shares = video_shares;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(video_id);
        dest.writeString(video_url);
        dest.writeString(video_user_id);
        dest.writeString(video_user_name);
        dest.writeString(video_user_photo);
        dest.writeString(video_user_followers);
        dest.writeString(video_likes);
        dest.writeString(video_comments);
        dest.writeString(video_shares);
    }
}
