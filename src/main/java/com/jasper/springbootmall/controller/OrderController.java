package com.jasper.springbootmall.controller;


import com.jasper.springbootmall.dto.CreateOrderRequest;
import com.jasper.springbootmall.model.Order;
import com.jasper.springbootmall.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    //取的URL中userId值,前端傳遞格式及參數
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
      //mysql 自動生成orderId,並插入數據
       Integer orderId=orderService.createOrder(userId,createOrderRequest);
        //從資料庫中查出訂單
       Order order=orderService.getOrderById(orderId);
       //回傳給前端
        return  ResponseEntity.status(HttpStatus.CREATED).body(order);


    }
}
