
package com.tetravalstartups.dingdong.modules.discover;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MostViewVideo {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<MostViewVideoResponse> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MostViewVideo() {
    }

    /**
     * 
     * @param data
     * @param status
     */
    public MostViewVideo(Boolean status, List<MostViewVideoResponse> data) {
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

    public List<MostViewVideoResponse> getData() {
        return data;
    }

    public void setData(List<MostViewVideoResponse> data) {
        this.data = data;
    }

}
