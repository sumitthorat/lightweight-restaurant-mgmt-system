package com.example.lmrs.model.managetables;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;

import java.util.ArrayList;
import java.util.List;

public class ManageTablesModel {
    ApiInterface apiInterface;

    static private final String TAG = "ManageTablesModel";

    public ManageTablesModel() {
        apiInterface = ApiUtils.getApiInterface();
    }

    public List<Table> getTables() {
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
}
