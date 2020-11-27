package com.example.lmrs.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * POJO for Get Current Day Sale API Response
 */
public class GetCurrentDaySaleJSONResponse {

    @SerializedName("current_day_sale")
    @Expose
    private Integer currentDaySale;

    public Integer getCurrentDaySale() {
        return currentDaySale;
    }

    public void setCurrentDaySale(Integer currentDaySale) {
        this.currentDaySale = currentDaySale;
    }

}
