
package com.tetravalstartups.dingdong.modules.profile.videos.liked;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;

public class LikedVideos {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<VideoResponseDatum> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<VideoResponseDatum> getData() {
        return data;
    }

    public void setData(List<VideoResponseDatum> data) {
        this.data = data;
    }

}
