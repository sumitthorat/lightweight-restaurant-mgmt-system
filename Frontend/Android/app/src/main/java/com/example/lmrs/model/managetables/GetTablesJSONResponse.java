package com.example.lmrs.model.managetables;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTablesJSONResponse {

    @SerializedName("qrcode_str")
    @Expose
    private String qrcodeStr;
    @SerializedName("tableid")
    @Expose
    private Integer tableid;

    public String getQrcodeStr() {
        return qrcodeStr;
    }

    public void setQrcodeStr(String qrcodeStr) {
        this.qrcodeStr = qrcodeStr;
    }

    public Integer getTableid() {
        return tableid;
    }

    public void setTableid(Integer tableid) {
        this.tableid = tableid;
    }

}
