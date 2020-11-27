package com.example.lmrs.model.vieworders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO for Get Pending Orders JSON Response
 */
public class GetPendingOrdersResponseJSON {

    @SerializedName("items")
    @Expose
    private List<ItemJSON> items = null;
    @SerializedName("orderid")
    @Expose
    private Integer orderid;
    @SerializedName("tableid")
    @Expose
    private Integer tableid;

    public List<ItemJSON> getItemJSONS() {
        return items;
    }

    public void setItems(List<ItemJSON> items) {
        this.items = items;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getTableid() {
        return tableid;
    }

    public void setTableid(Integer tableid) {
        this.tableid = tableid;
    }

}

