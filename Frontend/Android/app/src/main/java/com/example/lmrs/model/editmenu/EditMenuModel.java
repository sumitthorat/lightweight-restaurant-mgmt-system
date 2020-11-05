package com.example.lmrs.model.editmenu;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditMenuModel {

    final static String TAG = "EditMenuModel";

    ApiInterface apiInterface;

    public EditMenuModel() {
        apiInterface = ApiUtils.getApiInterface();
    }

    public List<String> getAllCategories(String[] err) {
        GetCategoriesJSONResponse getCategoriesJSONResponse = null;
        try {
            getCategoriesJSONResponse = apiInterface.getAllCategories().execute().body();
            assert getCategoriesJSONResponse != null;
            if (getCategoriesJSONResponse.getStatus() == -1) {
                err[0] = getCategoriesJSONResponse.getMessage();
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        assert getCategoriesJSONResponse != null;
        return getCategoriesJSONResponse.getCategories();
    }

    public boolean addNewItem(String itemName, String category, Integer price, String[] err) {
        AddItemJSONRequest addItemJSONRequest = new AddItemJSONRequest();

        addItemJSONRequest.setCategory(category);
        addItemJSONRequest.setItemName(itemName);
        addItemJSONRequest.setPrice(price);

        AddItemJSONResponse addItemJSONResponse = null;

        try {
            addItemJSONResponse = apiInterface.addItemToMenu(addItemJSONRequest).execute().body();
            assert addItemJSONResponse != null;
            if (addItemJSONResponse.getStatus() == -1) {
                err[0] = addItemJSONResponse.getMessage();
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return addItemJSONResponse != null && addItemJSONResponse.getStatus() != -1;
    }

    public Map<String, List<MenuItem>> getFullMenu() {
        GetFullMenuJSONResponse getFullMenuJSONResponse = null;
        Map<String, List<MenuItem>> res = new HashMap<>();

        try {
            getFullMenuJSONResponse = apiInterface.getFullMenu().execute().body();
            assert getFullMenuJSONResponse != null;
            List<Category> categoryItems = getFullMenuJSONResponse.getCategories();
            for (Category category: categoryItems) {
                res.put(category.getCategory(), category.getMenuItems());
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return res;
    }
}
