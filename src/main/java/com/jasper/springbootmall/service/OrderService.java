package com.jasper.springbootmall.service;

import com.jasper.springbootmall.dto.CreateOrderRequest;

public interface OrderService {

     Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

}
