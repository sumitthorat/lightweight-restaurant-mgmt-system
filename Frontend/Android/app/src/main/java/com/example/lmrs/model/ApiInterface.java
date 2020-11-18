package com.example.lmrs.model;

import com.example.lmrs.model.editmenu.AddItemJSONRequest;
import com.example.lmrs.model.editmenu.AddItemJSONResponse;
import com.example.lmrs.model.editmenu.GetCategoriesJSONResponse;
import com.example.lmrs.model.editmenu.GetFullMenuJSONResponse;
import com.example.lmrs.model.login.AttemptLoginJSONRequest;
import com.example.lmrs.model.login.LoginJSONResponse;
import com.example.lmrs.model.login.NewUserJSONRequest;
import com.example.lmrs.model.managetables.AddDeleteTableJSONRequest;
import com.example.lmrs.model.managetables.AddDeleteTableJSONResponse;
import com.example.lmrs.model.managetables.GetTablesJSONResponse;
import com.example.lmrs.model.statistics.GetCurrentDaySaleJSONResponse;
import com.example.lmrs.model.statistics.GetItemSaleJSONRequest;
import com.example.lmrs.model.statistics.GetItemSaleJSONResponse;
import com.example.lmrs.model.statistics.GetMostSoldItemJSONResponse;
import com.example.lmrs.model.statistics.GetOrderAvgTimeJSONResponse;
import com.example.lmrs.model.vieworders.GetPendingOrdersResponseJSON;
import com.example.lmrs.model.vieworders.OrderCompleteJSONRequest;
import com.example.lmrs.model.vieworders.OrderCompleteJSONResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface  ApiInterface {

    // API calls interface for LoginModel

    @Headers("Content-Type: application/json")
    @PUT("AttemptLogin")
    Call<LoginJSONResponse> attemptLogin(@Body AttemptLoginJSONRequest attemptLoginJSONRequest);

    @Headers("Content-Type: application/json")
    @PUT("AddNewUser")
    Call<LoginJSONResponse> addNewUser(@Body NewUserJSONRequest newUserJSONRequest);

    @GET("GetCategories")
    Call<GetCategoriesJSONResponse> getAllCategories();

    @GET("GetFullMenu")
    Call<GetFullMenuJSONResponse> getFullMenu();

    @PUT("AddItemToMenu")
    Call<AddItemJSONResponse> addItemToMenu(@Body AddItemJSONRequest addItemJSONRequest);

    @GET("GetPendingOrders")
    Call<List<GetPendingOrdersResponseJSON>> getPendingOrders();

    @PUT("OrderComplete")
    Call<OrderCompleteJSONResponse> notifyOrderComplete(@Body OrderCompleteJSONRequest orderCompleteJSONRequest);

    @GET("GetTables")
    Call<List<GetTablesJSONResponse>> getTablesInfo();
    
    @PUT("AddTable")
    Call<AddDeleteTableJSONResponse> addTable(@Body AddDeleteTableJSONRequest addDeleteTableJSONRequest);

    @PUT("DeleteTable")
    Call<AddDeleteTableJSONResponse> deleteTable(@Body AddDeleteTableJSONRequest addDeleteTableJSONRequest);

    @PUT("ItemSale")
    Call<GetItemSaleJSONResponse> getItemSale(@Body GetItemSaleJSONRequest getItemSaleJSONRequest);

    @GET("CurrentDaySale")
    Call<GetCurrentDaySaleJSONResponse> getCurrentDaySale();

    @GET("AvgOrderTime")
    Call<GetOrderAvgTimeJSONResponse> getAvgOrderTime();

    @GET("MostSoldItem")
    Call<GetMostSoldItemJSONResponse> getMostSoldItem();

}
