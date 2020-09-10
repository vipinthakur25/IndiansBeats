package com.tetravalstartups.dingdong.modules.profile.model.following;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tetravalstartups.dingdong.modules.profile.model.followers.FollowersResponse;

import java.util.List;

public class Following {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<FollowingResponse> followingResponses = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<FollowingResponse> getData() {
        return followingResponses;
    }

    public void setData(List<FollowingResponse> followingResponses) {
        this.followingResponses = followingResponses;
    }

}