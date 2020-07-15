package com.tetravalstartups.dingdong.modules.home.video;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Video {
    public static final int MAX_VIDEO_DURATION = 60000;

    private String id;
    private String video_desc;
    private String sound_id;
    private String sound_title;
    private String likes_count;
    private String share_count;
    private String comment_count;
    private String user_id;
    private String user_handle;
    private String user_photo;
    private String video_thumbnail;
    private String video_status;

    public Video() {
    }

    public Video(String id, String video_desc, String sound_id, String sound_title, String likes_count, String share_count, String comment_count, String user_id, String user_handle, String user_photo, String video_thumbnail, String video_status) {
        this.id = id;
        this.video_desc = video_desc;
        this.sound_id = sound_id;
        this.sound_title = sound_title;
        this.likes_count = likes_count;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.user_id = user_id;
        this.user_handle = user_handle;
        this.user_photo = user_photo;
        this.video_thumbnail = video_thumbnail;
        this.video_status = video_status;
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

    public String getVideo_status() {
        return video_status;
    }

    public void setVideo_status(String video_status) {
        this.video_status = video_status;
    }
}
