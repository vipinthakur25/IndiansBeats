package com.tetravalstartups.dingdong.modules.subscription;

public class SubPlan {

    private String id;
    private String name;
    private String start_date;
    private String end_date;
    private String total_uploads;
    private String avl_uploads;
    private String monthly_profit;
    private String status;

    public SubPlan() {
    }

    public SubPlan(String id, String name, String start_date, String end_date, String total_uploads, String avl_uploads, String monthly_profit, String status) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.total_uploads = total_uploads;
        this.avl_uploads = avl_uploads;
        this.monthly_profit = monthly_profit;
        this.status = status;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getTotal_uploads() {
        return total_uploads;
    }

    public void setTotal_uploads(String total_uploads) {
        this.total_uploads = total_uploads;
    }

    public String getAvl_uploads() {
        return avl_uploads;
    }

    public void setAvl_uploads(String avl_uploads) {
        this.avl_uploads = avl_uploads;
    }

    public String getMonthly_profit() {
        return monthly_profit;
    }

    public void setMonthly_profit(String monthly_profit) {
        this.monthly_profit = monthly_profit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
