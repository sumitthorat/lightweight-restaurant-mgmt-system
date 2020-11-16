package com.example.lmrs.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOrderAvgTimeJSONResponse {

    @SerializedName("avg_time")
    @Expose
    private String avgTime;
    @SerializedName("orders_completed")
    @Expose
    private Integer ordersCompleted;

    public String getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(String avgTime) {
        this.avgTime = avgTime;
    }

    public Integer getOrdersCompleted() {
        return ordersCompleted;
    }

    public void setOrdersCompleted(Integer ordersCompleted) {
        this.ordersCompleted = ordersCompleted;
    }

}
