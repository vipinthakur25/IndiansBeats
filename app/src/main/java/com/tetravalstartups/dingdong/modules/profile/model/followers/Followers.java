package com.tetravalstartups.dingdong.modules.profile.model.followers;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Followers {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<FollowersResponse> followersResponses = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<FollowersResponse> getData() {
        return followersResponses;
    }

    public void setData(List<FollowersResponse> followersResponses) {
        this.followersResponses = followersResponses;
    }

}