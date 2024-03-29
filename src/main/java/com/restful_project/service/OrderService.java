package com.restful_project.service;

import com.restful_project.entity.Order;
import com.restful_project.entity.Specification;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(Order order);

    Order updateOrder(Long id, Order updatedOrder);

    void deleteOrder(Long id);
}
