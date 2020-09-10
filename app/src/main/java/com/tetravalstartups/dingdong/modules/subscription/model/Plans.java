package com.tetravalstartups.dingdong.modules.subscription.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Plans {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<PlanResponse> planResponseList = null;

    /**
     * No args constructor for use in serialization
     */
    public Plans() {
    }

    /**
     * @param planResponseList
     * @param status
     */
    public Plans(Boolean status, List<PlanResponse> planResponseList) {
        super();
        this.status = status;
        this.planResponseList = planResponseList;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<PlanResponse> getPlanResponseList() {
        return planResponseList;
    }

    public void setData(List<PlanResponse> planResponseList) {
        this.planResponseList = planResponseList;
    }

}