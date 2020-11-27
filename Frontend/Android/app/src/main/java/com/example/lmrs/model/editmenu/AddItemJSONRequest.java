package com.example.lmrs.model.editmenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * POJO for Add Item API Request
 */
public class AddItemJSONRequest {

    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("category")
    @Expose
    private String category;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
