package com.tetravalstartups.dingdong.modules.subscription.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MySubscriptionResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("total_uploads")
    @Expose
    private Integer totalUploads;
    @SerializedName("avl_uploads")
    @Expose
    private Integer avlUploads;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("monthly_profit")
    @Expose
    private Integer monthlyProfit;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("plan_id")
    @Expose
    private Integer planId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("transection_id")
    @Expose
    private String transectionId;

    /**
     * No args constructor for use in serialization
     */
    public MySubscriptionResponse() {
    }

    /**
     * @param endDate
     * @param name
     * @param monthlyProfit
     * @param transectionId
     * @param planId
     * @param id
     * @param totalUploads
     * @param userId
     * @param startDate
     * @param avlUploads
     * @param status
     */
    public MySubscriptionResponse(String id, String name, Integer totalUploads, Integer avlUploads, String startDate, String endDate, Integer monthlyProfit, Integer status, Integer planId, String userId, String transectionId) {
        super();
        this.id = id;
        this.name = name;
        this.totalUploads = totalUploads;
        this.avlUploads = avlUploads;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyProfit = monthlyProfit;
        this.status = status;
        this.planId = planId;
        this.userId = userId;
        this.transectionId = transectionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalUploads() {
        return totalUploads;
    }

    public void setTotalUploads(Integer totalUploads) {
        this.totalUploads = totalUploads;
    }

    public Integer getAvlUploads() {
        return avlUploads;
    }

    public void setAvlUploads(Integer avlUploads) {
        this.avlUploads = avlUploads;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getMonthlyProfit() {
        return monthlyProfit;
    }

    public void setMonthlyProfit(Integer monthlyProfit) {
        this.monthlyProfit = monthlyProfit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransectionId() {
        return transectionId;
    }

    public void setTransectionId(String transectionId) {
        this.transectionId = transectionId;
    }

}

