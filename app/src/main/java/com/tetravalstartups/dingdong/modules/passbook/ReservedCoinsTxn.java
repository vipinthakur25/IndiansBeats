package com.tetravalstartups.dingdong.modules.passbook;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ReservedCoinsTxn implements Parcelable {
    @ServerTimestamp
    private Date timestamp;
    private String id;
    private int type;
    private int amount;
    private String time;
    private String date;
    private String remark;
    private int status;

    public ReservedCoinsTxn() {
    }

    public ReservedCoinsTxn(Date timestamp, String id, int type, int amount, String time, String date, String remark, int status) {
        this.timestamp = timestamp;
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.date = date;
        this.remark = remark;
        this.status = status;
    }

    protected ReservedCoinsTxn(Parcel in) {
        id = in.readString();
        type = in.readInt();
        amount = in.readInt();
        time = in.readString();
        date = in.readString();
        remark = in.readString();
        status = in.readInt();
    }

    public static final Creator<ReservedCoinsTxn> CREATOR = new Creator<ReservedCoinsTxn>() {
        @Override
        public ReservedCoinsTxn createFromParcel(Parcel in) {
            return new ReservedCoinsTxn(in);
        }

        @Override
        public ReservedCoinsTxn[] newArray(int size) {
            return new ReservedCoinsTxn[size];
        }
    };

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(type);
        dest.writeInt(amount);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(remark);
        dest.writeInt(status);
    }
}
