package com.tetravalstartups.dingdong.modules.home.video;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Video implements Parcelable {
    public static final int MAX_VIDEO_DURATION = 60000;

    @ServerTimestamp
    private Date timestamp;
    private String id;
    private String video_desc;
    private String sound_id;
    private String sound_title;
    private String likes_count;
    private String share_count;
    private String comment_count;
    private String view_count;
    private String user_id;
    private String user_handle;
    private String user_name;
    private String user_photo;
    private String video_thumbnail;
    private String video_url;
    private String video_status;
    private String video_index;

    public Video() {
    }

    public Video(Date timestamp, String id, String video_desc, String sound_id, String sound_title, String likes_count, String share_count, String comment_count, String view_count, String user_id, String user_handle, String user_name, String user_photo, String video_thumbnail, String video_url, String video_status, String video_index) {
        this.timestamp = timestamp;
        this.id = id;
        this.video_desc = video_desc;
        this.sound_id = sound_id;
        this.sound_title = sound_title;
        this.likes_count = likes_count;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.view_count = view_count;
        this.user_id = user_id;
        this.user_handle = user_handle;
        this.user_name = user_name;
        this.user_photo = user_photo;
        this.video_thumbnail = video_thumbnail;
        this.video_url = video_url;
        this.video_status = video_status;
        this.video_index = video_index;
    }

    protected Video(Parcel in) {
        id = in.readString();
        video_desc = in.readString();
        sound_id = in.readString();
        sound_title = in.readString();
        likes_count = in.readString();
        share_count = in.readString();
        comment_count = in.readString();
        view_count = in.readString();
        user_id = in.readString();
        user_handle = in.readString();
        user_name = in.readString();
        user_photo = in.readString();
        video_thumbnail = in.readString();
        video_url = in.readString();
        video_status = in.readString();
        video_index = in.readString();
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

    public String getVideo_desc() {
        return video_desc;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public String getSound_id() {
        return sound_id;
    }

    public void setSound_id(String sound_id) {
        this.sound_id = sound_id;
    }

    public String getSound_title() {
        return sound_title;
    }

    public void setSound_title(String sound_title) {
        this.sound_title = sound_title;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getShare_count() {
        return share_count;
    }

    public void setShare_count(String share_count) {
        this.share_count = share_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_handle() {
        return user_handle;
    }

    public void setUser_handle(String user_handle) {
        this.user_handle = user_handle;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_status() {
        return video_status;
    }

    public void setVideo_status(String video_status) {
        this.video_status = video_status;
    }

    public String getVideo_index() {
        return video_index;
    }

    public void setVideo_index(String video_index) {
        this.video_index = video_index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(video_desc);
        dest.writeString(sound_id);
        dest.writeString(sound_title);
        dest.writeString(likes_count);
        dest.writeString(share_count);
        dest.writeString(comment_count);
        dest.writeString(view_count);
        dest.writeString(user_id);
        dest.writeString(user_handle);
        dest.writeString(user_name);
        dest.writeString(user_photo);
        dest.writeString(video_thumbnail);
        dest.writeString(video_url);
        dest.writeString(video_status);
        dest.writeString(video_index);
    }
}
