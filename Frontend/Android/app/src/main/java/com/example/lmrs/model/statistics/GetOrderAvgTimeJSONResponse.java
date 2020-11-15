package com.example.lmrs.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOrderAvgTimeJSONResponse {

    @SerializedName("avg_time")
    @Expose
    private Double avgTime;

    public Double getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(Double avgTime) {
        this.avgTime = avgTime;
    }

}
