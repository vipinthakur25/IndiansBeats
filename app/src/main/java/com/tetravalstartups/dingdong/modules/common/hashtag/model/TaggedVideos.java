package com.tetravalstartups.dingdong.modules.common.hashtag.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;

public class TaggedVideos {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<VideoResponseDatum> data = null;

    /**
     * No args constructor for use in serialization
     */
    public TaggedVideos() {
    }

    /**
     * @param data
     * @param message
     * @param status
     */
    public TaggedVideos(Boolean status, String message, List<VideoResponseDatum> data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<VideoResponseDatum> getData() {
        return data;
    }

    public void setData(List<VideoResponseDatum> data) {
        this.data = data;
    }

}