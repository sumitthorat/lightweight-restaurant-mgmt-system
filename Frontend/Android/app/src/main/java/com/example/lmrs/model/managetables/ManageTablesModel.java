package com.example.lmrs.model.managetables;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * "Brain" for the manage tables page
 */
public class ManageTablesModel {

    ApiInterface apiInterface;

    static private final String TAG = "ManageTablesModel";

    public ManageTablesModel() {
        apiInterface = ApiUtils.getApiInterface();
    }

    public List<Table> getTables() {
        /**
         * Retrieve all tables from the server
         */
        List<GetTablesJSONResponse> responses = null;
        List<Table> ret = null;
        try {
            responses = apiInterface.getTablesInfo().execute().body();
            Log.i(TAG, "Response size: " + responses.size());

            if (responses != null) {
                ret = new ArrayList<>();

                for (GetTablesJSONResponse resp: responses) {
                    ret.add(new Table(resp.getTableid(), resp.getQrcodeStr()));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return ret;
    }

    public boolean addTable(Integer tableId, String[] err) {
        /**
         * Add new table to the database
         */
        AddDeleteTableJSONRequest request = new AddDeleteTableJSONRequest();
        request.setTableid(tableId);
        AddDeleteTableJSONResponse response = null;
        try {
            response = apiInterface.addTable(request).execute().body();

            if (response != null) {
                if (response.getStatus() == -1) {
                    err[0] = response.getMessage();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return response != null && response.getStatus() != -1;
    }

    public boolean deleteTable(Integer tableId, String[] err) {
        /**
         * Delete a table based on the table id
         */
        AddDeleteTableJSONRequest request = new AddDeleteTableJSONRequest();
        request.setTableid(tableId);
        AddDeleteTableJSONResponse response = null;
        try {
            response = apiInterface.deleteTable(request).execute().body();

            if (response != null) {
                if (response.getStatus() == -1) {
                    err[0] = response.getMessage();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return response != null && response.getStatus() != -1;
    }
}
