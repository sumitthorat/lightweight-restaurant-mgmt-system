package com.example.lmrs.model.managetables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * POJO for Table
 */
public class Table {
    Integer tableId;
    Bitmap qrCode;

    public Table(Integer tableId, String encodedQr) {
        this.tableId = tableId;
        this.qrCode = decodeString(encodedQr);
    }

    Bitmap decodeString(String code) {
        int len = code.length();
        code = code.substring(2, len - 1);
        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public Integer getTableId() {
        return tableId;
    }

    public Bitmap getQrCode() {
        return qrCode;
    }
}
