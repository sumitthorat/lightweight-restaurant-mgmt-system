package com.example.lmrs.model.vieworders;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewOrdersModel {
    ApiInterface apiInterface;

    final String TAG = "ViewOrdersModel";

    public ViewOrdersModel() {
        apiInterface = ApiUtils.getApiInterface();
    }



    public List<Order> getPendingOrders(String[] err) {
        List<GetPendingOrdersResponseJSON> ordersList = null;
        List<Order> orders = null;
        try {
            ordersList = apiInterface.getPendingOrders().execute().body();
            orders = new ArrayList<>();

            for (GetPendingOrdersResponseJSON orderJSON: ordersList) {
                List<OrderItem> orderItems = new ArrayList<>();
                for (ItemJSON itemJSON: orderJSON.getItemJSONS()) {
                    OrderItem orderItem = new OrderItem(itemJSON.getItemName(), itemJSON.getItemQty());
                    orderItems.add(orderItem);
                }
                Order o = new Order(String.valueOf(orderJSON.getOrderid()), orderItems);
                orders.add(o);
            }

            Collections.sort(orders);

        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }

        return orders;
    }

}
