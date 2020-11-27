package com.example.lmrs.model.managetables;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for Add/Delete table API Request
 */

public class AddDeleteTableJSONRequest {

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
