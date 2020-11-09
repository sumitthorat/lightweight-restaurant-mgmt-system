package com.example.lmrs.model.vieworders;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ViewOrdersModel {
    ApiInterface apiInterface;
    Socket mSocket;

    ViewOrdersProtocol delegate;

    final String TAG = "ViewOrdersModel";

    public ViewOrdersModel() {
        apiInterface = ApiUtils.getApiInterface();

        try {
            mSocket = IO.socket("http://10.0.2.2:5000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on("new order", onNewOrder);
        mSocket.connect();
    }

    Emitter.Listener onNewOrder = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject jsonObject = (JSONObject) args[0];

            try {
                JSONArray itemsArrayJSON = jsonObject.getJSONArray("items");

                List<OrderItem> orderItems = new ArrayList<>();

                for (int i = 0; i < itemsArrayJSON.length(); ++i) {
                    JSONObject item = (JSONObject) itemsArrayJSON.get(i);

                    String itemName = item.getString("item_name");
                    int itemQty = item.getInt("item_qty");

                    OrderItem orderItem = new OrderItem(itemName, itemQty);
                    orderItems.add(orderItem);
                }
                Order o = new Order(String.valueOf((int) jsonObject.get("orderid")), orderItems);

                delegate.onReceiveNewOrder(o);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void setDelegate(ViewOrdersProtocol delegate) {
        this.delegate = delegate;
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

    public boolean notifyOrderComplete(Integer orderId, String[] err) {
        OrderCompleteJSONResponse response = null;
        OrderCompleteJSONRequest request = new OrderCompleteJSONRequest();
        request.setOrderid(orderId);
        try {
            response = apiInterface.notifyOrderComplete(request).execute().body();
            assert response != null;
            if (response.getStatus() == -1) {
                err[0] = response.getMessage();
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);
        }
        assert response != null;
        return response.getStatus() != -1;
    }


}
