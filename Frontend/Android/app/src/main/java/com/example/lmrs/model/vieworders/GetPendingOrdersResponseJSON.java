package com.example.lmrs.model.vieworders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPendingOrdersResponseJSON {

    @SerializedName("items")
    @Expose
    private List<ItemJSON> itemJSONS = null;
    @SerializedName("orderid")
    @Expose
    private Integer orderid;

    public List<ItemJSON> getItemJSONS() {
        return itemJSONS;
    }

    public void setItemJSONS(List<ItemJSON> itemJSONS) {
        this.itemJSONS = itemJSONS;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

}

