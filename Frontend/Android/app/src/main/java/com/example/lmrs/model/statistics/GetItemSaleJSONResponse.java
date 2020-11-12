package com.example.lmrs.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetItemSaleJSONResponse {

    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("quantity_sold")
    @Expose
    private Integer quantitySold;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

}
