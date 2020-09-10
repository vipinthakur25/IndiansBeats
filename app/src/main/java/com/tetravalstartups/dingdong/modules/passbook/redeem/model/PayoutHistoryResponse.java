package com.tetravalstartups.dingdong.modules.passbook.redeem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayoutHistoryResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    /**
     * No args constructor for use in serialization
     */
    public PayoutHistoryResponse() {
    }

    /**
     * @param amount
     * @param createdDate
     * @param description
     * @param id
     * @param type
     * @param userId
     * @param status
     */
    public PayoutHistoryResponse(String id, String userId, Integer amount, Integer type, String description, Integer status, String createdDate) {
        super();
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}