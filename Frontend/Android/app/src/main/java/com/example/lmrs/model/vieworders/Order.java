package com.example.lmrs.model.vieworders;

import java.util.List;

public class Order {
    private String tableId, orderId;
    private List<OrderItem> orderItemList;


    public Order(String tableId, String orderId, List<OrderItem> orderItemList) {
        this.tableId = tableId;
        this.orderId = orderId;
        this.orderItemList = orderItemList;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
