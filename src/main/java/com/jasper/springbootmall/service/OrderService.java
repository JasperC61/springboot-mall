package com.jasper.springbootmall.service;


import com.jasper.springbootmall.dto.CreateOrderRequest;
import com.jasper.springbootmall.dto.OrderQueryParams;
import com.jasper.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
       Integer countOrder(OrderQueryParams orderQueryParams);

       List<Order> getOrders(OrderQueryParams orderQueryParams);

       Order getOrderById(Integer orderId);
       Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
