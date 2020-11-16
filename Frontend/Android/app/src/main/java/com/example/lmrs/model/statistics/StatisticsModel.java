package com.example.lmrs.model.statistics;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;

public class StatisticsModel {

    ApiInterface apiInterface;



    static private final String TAG = "StatisticsModel";

    public StatisticsModel() {
        apiInterface = ApiUtils.getApiInterface();
    }

    public int getItemSale(String itemName, int days, String[] err) {
        GetItemSaleJSONRequest request = new GetItemSaleJSONRequest();
        request.setItemName(itemName);
        request.setDays(days);

        GetItemSaleJSONResponse response = null;

        try {
            response = apiInterface.getItemSale(request).execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return response == null ? -1 : response.getQuantitySold();
    }

    public int getTodaysSale() {
        GetCurrentDaySaleJSONResponse response = null;
        try {
            response = apiInterface.getCurrentDaySale().execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }
        return response == null ? -1 : response.getCurrentDaySale();
    }

    public String getMostSoldItem(String[] err) {
        GetMostSoldItemJSONResponse response = null;
        try {
            response = apiInterface.getMostSoldItem().execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }
        return response == null ? "" : response.getItemName();
    }

    public String getAvgOrderTime() {
        GetOrderAvgTimeJSONResponse response = null;
        try {
            response = apiInterface.getAvgOrderTime().execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return response == null ? "" : response.getAvgTime();
    }

}
