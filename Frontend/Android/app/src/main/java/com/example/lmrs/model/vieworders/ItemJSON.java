package com.example.lmrs.model.vieworders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemJSON {

    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_qty")
    @Expose
    private Integer itemQty;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

}
