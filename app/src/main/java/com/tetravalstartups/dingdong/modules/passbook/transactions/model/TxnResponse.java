package com.tetravalstartups.dingdong.modules.passbook.transactions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxnResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    /**
     * No args constructor for use in serialization
     */
    public TxnResponse() {
    }

    /**
     * @param date
     * @param amount
     * @param remark
     * @param id
     * @param time
     * @param userId
     * @param status
     * @param timestamp
     */
    public TxnResponse(String id, String userId, Integer amount, String remark, String date, String time, Integer status, String timestamp) {
        super();
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.remark = remark;
        this.date = date;
        this.time = time;
        this.status = status;
        this.timestamp = timestamp;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}