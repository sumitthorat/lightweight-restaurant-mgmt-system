package com.example.lmrs.model.vieworders;

import java.util.List;

public class Order implements Comparable<Order> {
    private String orderId;
    private String tableId;
    private List<OrderItem> orderItemList;


    public Order(String orderId, List<OrderItem> orderItemList, String tableId) {
        this.orderId = orderId;
        this.orderItemList = orderItemList;
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

    public String getTableId() {
        return this.tableId;
    }

    @Override
    public int compareTo(Order o) {
        return this.getOrderId().compareTo(o.getOrderId());
    }
}
