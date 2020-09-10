package com.tetravalstartups.dingdong.modules.passbook;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Transactions {
    @ServerTimestamp
    private Date timestamp;
    private String id;
    private String amount;
    private String time;
    private String date;
    private String remark;
    private String status;

    public Transactions() {
    }

    public Transactions(Date timestamp, String id, String amount, String time, String date, String remark, String status) {
        this.timestamp = timestamp;
        this.id = id;
        this.amount = amount;
        this.time = time;
        this.date = date;
        this.remark = remark;
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
