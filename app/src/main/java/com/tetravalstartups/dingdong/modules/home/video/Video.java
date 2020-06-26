package com.tetravalstartups.dingdong.modules.home.video;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    private String id;
    private String video_desc;
    private boolean sound_contain;
    private String sound_id;
    private String sound_title;
    private String sound_url;
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

    public Video(String id, String video_desc, boolean sound_contain, String sound_id, String sound_title, String sound_url, String likes_count, String share_count, String comment_count, String user_id, String user_handle, String user_photo, String video_thumbnail, String video_status) {
        this.id = id;
        this.video_desc = video_desc;
        this.sound_contain = sound_contain;
        this.sound_id = sound_id;
        this.sound_title = sound_title;
        this.sound_url = sound_url;
        this.likes_count = likes_count;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.user_id = user_id;
        this.user_handle = user_handle;
        this.user_photo = user_photo;
        this.video_thumbnail = video_thumbnail;
        this.video_status = video_status;
    }

    protected Video(Parcel in) {
        id = in.readString();
        video_desc = in.readString();
        sound_contain = in.readByte() != 0;
        sound_id = in.readString();
        sound_title = in.readString();
        sound_url = in.readString();
        likes_count = in.readString();
        share_count = in.readString();
        comment_count = in.readString();
        user_id = in.readString();
        user_handle = in.readString();
        user_photo = in.readString();
        video_thumbnail = in.readString();
        video_status = in.readString();
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

    public boolean isSound_contain() {
        return sound_contain;
    }

    public void setSound_contain(boolean sound_contain) {
        this.sound_contain = sound_contain;
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

    public String getSound_url() {
        return sound_url;
    }

    public void setSound_url(String sound_url) {
        this.sound_url = sound_url;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(video_desc);
        dest.writeByte((byte) (sound_contain ? 1 : 0));
        dest.writeString(sound_id);
        dest.writeString(sound_title);
        dest.writeString(sound_url);
        dest.writeString(likes_count);
        dest.writeString(share_count);
        dest.writeString(comment_count);
        dest.writeString(user_id);
        dest.writeString(user_handle);
        dest.writeString(user_photo);
        dest.writeString(video_thumbnail);
        dest.writeString(video_status);
    }
}
