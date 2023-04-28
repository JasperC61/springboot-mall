package com.jasper.springbootmall.controller;


import com.jasper.springbootmall.dto.CreateOrderRequest;
import com.jasper.springbootmall.dto.OrderQueryParams;
import com.jasper.springbootmall.model.Order;
import com.jasper.springbootmall.service.OrderService;
import com.jasper.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Max(1000)@Min(0) Integer limit,
            @RequestParam(defaultValue = "0")@Min(0) Integer offset
    ){
        OrderQueryParams orderQueryParams=new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        //取得 order list
        List<Order> orderList=orderService.getOrders(orderQueryParams);
        //取得 order總數
        Integer count=orderService.countOrder(orderQueryParams);

        //分頁
        Page<Order> page=new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);
        return ResponseEntity.status(HttpStatus.OK).body(page);

    }

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
