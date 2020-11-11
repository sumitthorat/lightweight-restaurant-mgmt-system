package com.example.lmrs.model.managetables;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTableJSONRequest {

    @SerializedName("tableid")
    @Expose
    private Integer tableid;

    public Integer getTableid() {
        return tableid;
    }

    public void setTableid(Integer tableid) {
        this.tableid = tableid;
    }

}
