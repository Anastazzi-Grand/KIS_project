package com.restful_project.service.impl;

import com.restful_project.entity.Order;
import com.restful_project.entity.Specification;
import com.restful_project.entity.Storage;
import com.restful_project.repository.OrderRepository;
import com.restful_project.repository.SpecificationRepository;
import com.restful_project.repository.StorageRepository;
import com.restful_project.service.OrderService;
import com.restful_project.service.StorageService;
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

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageRepository storageRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден с id: " + id));
    }

    @Override
    public Order createOrder(Order order) {
        Specification specification = order.getSpecificationId();
        if (specification != null && specification.getPositionid() != null) {
            Long specificationId = specification.getPositionid();
            Specification existingSpecification = specificationRepository.findById(specificationId).orElseThrow(() -> new IllegalArgumentException("Спецификация с ID " + specificationId + " не найдена"));
            order.setSpecificationId(existingSpecification);
        }
        // Проверяем наличие товаров на складе
       // placeOrder(order);

        /*
        if (!orderPlaced) {
            // Если заказ не может быть выполнен из-за нехватки товаров на складе
            throw new RuntimeException("Невозможно оформить заказ, на складе недостаточно товаров");
        }*/

        // Сохраняем заказ в базе данных только если товаров достаточно

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
                .orElseThrow(() -> new IllegalArgumentException("Заказ с id " + id + " не найден"));
        orderRepository.delete(order);
    }

    public void placeOrder(Order order) {
        Specification specification = order.getSpecificationId();
        Integer orderQuantity = order.getCount();

        // Создаем новую запись в таблице storage с типом операции "отпуск"
        Storage outboundStorage = new Storage();
        outboundStorage.setDate(order.getOrderDate());
        outboundStorage.setQuantity(orderQuantity); // Отрицательное количество для отпуска
        outboundStorage.setTypeOfOperation("уход");
        outboundStorage.setSpecificationId(specification);
        storageRepository.save(outboundStorage);
    }
}
