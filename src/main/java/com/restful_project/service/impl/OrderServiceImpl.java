package com.restful_project.service.impl;

import com.restful_project.entity.Order;
import com.restful_project.entity.Specification;
import com.restful_project.repository.OrderRepository;
import com.restful_project.repository.SpecificationRepository;
import com.restful_project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SpecificationRepository specificationRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersSortedById() {
        return orderRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<Order> getAllOrdersSortedByClientName() {
        return orderRepository.findAll(Sort.by(Sort.Direction.ASC, "clientName"));
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public Order createOrder(Order order) {
        Specification specification = order.getSpecificationId();
        if (specification != null && specification.getPositionid() != null) {
            Long specificationId = specification.getPositionid();
            Specification existingSpecification = specificationRepository.findById(specificationId).orElseThrow(() -> new IllegalArgumentException("Спецификация с ID " + specificationId + " не найдена"));
            order.setSpecificationId(existingSpecification);
        }
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, Order existingOrder) {
        Order updatedOrder = getOrderById(id);

        updatedOrder.setOrderDate(existingOrder.getOrderDate());
        updatedOrder.setCount(existingOrder.getCount());
        updatedOrder.setClientName(existingOrder.getClientName());
        updatedOrder.setMeasureUnit(existingOrder.getMeasureUnit());

        Specification specification = existingOrder.getSpecificationId();
        if (specification != null) {
            Long specificationId = specification.getPositionid();
            if (specificationId != null) {
                Specification updatedSpecification = specificationRepository.findById(specificationId)
                        .orElseThrow(() -> new IllegalArgumentException("Спецификация с ID " + specificationId + " не найдена"));
                updatedOrder.setSpecificationId(updatedSpecification);
            } else {
                updatedOrder.setSpecificationId(null);
            }
        } else {
            updatedOrder.setSpecificationId(null);
        }

        return orderRepository.save(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с id " + id + " не найден"));
        orderRepository.delete(order);
    }
}
