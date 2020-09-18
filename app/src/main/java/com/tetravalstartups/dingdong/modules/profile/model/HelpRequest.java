package com.tetravalstartups.dingdong.modules.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpRequest {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<HelpRequestResponse> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public HelpRequest() {
    }

    /**
     *
     * @param data
     * @param status
     */
    public HelpRequest(Boolean status, List<HelpRequestResponse> data) {
        super();
        this.status = status;
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<HelpRequestResponse> getData() {
        return data;
    }

    public void setData(List<HelpRequestResponse> data) {
        this.data = data;
    }

}