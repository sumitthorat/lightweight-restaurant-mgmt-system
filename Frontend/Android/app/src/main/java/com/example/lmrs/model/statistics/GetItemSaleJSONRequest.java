package com.example.lmrs.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for Get Item Sale API Request
 */
public class GetItemSaleJSONRequest {

    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("days")
    @Expose
    private Integer days;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

}
