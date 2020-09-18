
package com.tetravalstartups.dingdong.modules.profile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyComplain {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<MyComplainResponse> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MyComplain() {
    }

    /**
     * 
     * @param data
     * @param status
     */
    public MyComplain(Boolean status, List<MyComplainResponse> data) {
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

    public List<MyComplainResponse> getData() {
        return data;
    }

    public void setData(List<MyComplainResponse> data) {
        this.data = data;
    }

}
