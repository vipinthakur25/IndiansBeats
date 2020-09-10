package com.tetravalstartups.dingdong.modules.publish;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableTask {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<AvailableTaskResponse> data = null;

    /**
     * No args constructor for use in serialization
     */
    public AvailableTask() {
    }

    /**
     * @param data
     * @param status
     */
    public AvailableTask(Boolean status, List<AvailableTaskResponse> data) {
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

    public List<AvailableTaskResponse> getData() {
        return data;
    }

    public void setData(List<AvailableTaskResponse> data) {
        this.data = data;
    }

}