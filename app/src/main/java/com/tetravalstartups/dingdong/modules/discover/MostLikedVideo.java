
package com.tetravalstartups.dingdong.modules.discover;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MostLikedVideo {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<MostLikedVideoResponse> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MostLikedVideo() {
    }

    /**
     * 
     * @param data
     * @param status
     */
    public MostLikedVideo(Boolean status, List<MostLikedVideoResponse> data) {
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

    public List<MostLikedVideoResponse> getData() {
        return data;
    }

    public void setData(List<MostLikedVideoResponse> data) {
        this.data = data;
    }

}
