package com.tetravalstartups.dingdong.modules.subscription;

import android.os.Parcel;
import android.os.Parcelable;

public class Subscription implements Parcelable {
    private String id;
    private String name;
    private int amount;
    private int validity;
    private String validity_unit;
    private int benefit;
    private String benefit_unit;
    private int total_benefit;
    private int uploads;
    private String upload_unit;

    public Subscription() {
    }

    public Subscription(String id, String name, int amount, int validity, String validity_unit, int benefit, String benefit_unit, int total_benefit, int uploads, String upload_unit) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.validity = validity;
        this.validity_unit = validity_unit;
        this.benefit = benefit;
        this.benefit_unit = benefit_unit;
        this.total_benefit = total_benefit;
        this.uploads = uploads;
        this.upload_unit = upload_unit;
    }

    protected Subscription(Parcel in) {
        id = in.readString();
        name = in.readString();
        amount = in.readInt();
        validity = in.readInt();
        validity_unit = in.readString();
        benefit = in.readInt();
        benefit_unit = in.readString();
        total_benefit = in.readInt();
        uploads = in.readInt();
        upload_unit = in.readString();
    }

    public static final Creator<Subscription> CREATOR = new Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel in) {
            return new Subscription(in);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public String getValidity_unit() {
        return validity_unit;
    }

    public void setValidity_unit(String validity_unit) {
        this.validity_unit = validity_unit;
    }

    public int getBenefit() {
        return benefit;
    }

    public void setBenefit(int benefit) {
        this.benefit = benefit;
    }

    public String getBenefit_unit() {
        return benefit_unit;
    }

    public void setBenefit_unit(String benefit_unit) {
        this.benefit_unit = benefit_unit;
    }

    public int getTotal_benefit() {
        return total_benefit;
    }

    public void setTotal_benefit(int total_benefit) {
        this.total_benefit = total_benefit;
    }

    public int getUploads() {
        return uploads;
    }

    public void setUploads(int uploads) {
        this.uploads = uploads;
    }

    public String getUpload_unit() {
        return upload_unit;
    }

    public void setUpload_unit(String upload_unit) {
        this.upload_unit = upload_unit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(amount);
        dest.writeInt(validity);
        dest.writeString(validity_unit);
        dest.writeInt(benefit);
        dest.writeString(benefit_unit);
        dest.writeInt(total_benefit);
        dest.writeInt(uploads);
        dest.writeString(upload_unit);
    }
}
