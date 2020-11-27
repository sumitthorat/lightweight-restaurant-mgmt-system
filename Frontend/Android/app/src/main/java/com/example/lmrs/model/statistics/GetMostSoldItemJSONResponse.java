package com.example.lmrs.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for Get Most Sold Item API Response
 */
public class GetMostSoldItemJSONResponse {

    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
