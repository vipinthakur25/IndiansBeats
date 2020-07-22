package com.tetravalstartups.dingdong.modules.comment;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class InVideoComment {
    @ServerTimestamp
    private Date timestamp;
    private String id;
    private String message;
    private String user_id;
    private String user_handle;
    private String user_photo;
    private String video_id;

    public InVideoComment() {
    }

    public InVideoComment(Date timestamp, String id, String message, String user_id, String user_handle, String user_photo, String video_id) {
        this.timestamp = timestamp;
        this.id = id;
        this.message = message;
        this.user_id = user_id;
        this.user_handle = user_handle;
        this.user_photo = user_photo;
        this.video_id = video_id;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
