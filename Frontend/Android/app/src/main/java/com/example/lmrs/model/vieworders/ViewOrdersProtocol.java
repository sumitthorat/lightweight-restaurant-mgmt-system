package com.example.lmrs.model.vieworders;

/**
 * Protocol for Deleage Pattern
 */
public interface ViewOrdersProtocol {
    void onReceiveNewOrder(Order order);
}
