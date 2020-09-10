package com.tetravalstartups.dingdong.modules.profile.external;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PublicProfile {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private PublicProfileResponse publicProfileResponse;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public PublicProfileResponse getPublicProfileResponse() {
        return publicProfileResponse;
    }

    public void setData(PublicProfileResponse publicProfileResponse) {
        this.publicProfileResponse = publicProfileResponse;
    }

}