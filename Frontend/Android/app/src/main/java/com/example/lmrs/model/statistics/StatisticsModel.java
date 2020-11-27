package com.example.lmrs.model.statistics;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;

/**
 * "Brain" for the statistics page
 */
public class StatisticsModel {
    ApiInterface apiInterface;



    static private final String TAG = "StatisticsModel";

    public StatisticsModel() {
        apiInterface = ApiUtils.getApiInterface();
    }

    public int getItemSale(String itemName, int days, String[] err) {
        /**
         * Get the quantity of items sold for a given item in the past number of "days"
         */
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
        /**
         * Calculate today's total sale
         */
        GetCurrentDaySaleJSONResponse response = null;
        try {
            response = apiInterface.getCurrentDaySale().execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }
        return response == null ? -1 : response.getCurrentDaySale();
    }

    public String getMostSoldItem(String[] err) {
        /**
         * Get most sold item of the day
         */
        GetMostSoldItemJSONResponse response = null;
        try {
            response = apiInterface.getMostSoldItem().execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }
        return response == null ? "" : response.getItemName();
    }

    public String getAvgOrderTime() {
        /**
         * Get the average order completion time
         */
        GetOrderAvgTimeJSONResponse response = null;
        try {
            response = apiInterface.getAvgOrderTime().execute().body();
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return response == null ? "" : response.getAvgTime();
    }

}
