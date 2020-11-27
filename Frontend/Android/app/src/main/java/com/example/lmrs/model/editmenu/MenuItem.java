package com.example.lmrs.model.editmenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for Menu Item
 */
public class MenuItem {

    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private Integer price;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
