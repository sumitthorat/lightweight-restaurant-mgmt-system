package com.example.lmrs.model.vieworders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderCompleteJSONRequest {

    @SerializedName("orderid")
    @Expose
    private Integer orderid;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

}
