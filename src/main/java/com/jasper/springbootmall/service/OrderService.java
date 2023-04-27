package com.jasper.springbootmall.service;


import com.jasper.springbootmall.dto.CreateOrderRequest;
import com.jasper.springbootmall.model.Order;

public interface OrderService {

       Order getOrderById(Integer orderId);
       Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
