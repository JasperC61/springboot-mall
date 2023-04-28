package com.jasper.springbootmall.service.impl;

import com.jasper.springbootmall.dao.OrderDao;
import com.jasper.springbootmall.dao.ProductDao;
import com.jasper.springbootmall.dao.UserDao;
import com.jasper.springbootmall.dao.impl.ProductDaoImpl;
import com.jasper.springbootmall.dto.BuyItem;
import com.jasper.springbootmall.dto.CreateOrderRequest;
import com.jasper.springbootmall.model.Order;
import com.jasper.springbootmall.model.OrderItem;
import com.jasper.springbootmall.model.Product;
import com.jasper.springbootmall.model.User;
import com.jasper.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;



@Component
public class OrderServiceImpl implements OrderService {
    private  final  static Logger log= LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDaoImpl productDao;
    @Autowired
    private UserDao userDao;

    //分別在order及order_item表中取的數據,所以會call orderDao兩次
    @Override
    public Order getOrderById(Integer orderId) {
        Order order=orderDao.getOrderById(orderId);
        List<OrderItem> orderItemList=orderDao.getOrderItemByOrderId(orderId);
        order.setOrderItemList(orderItemList);
        return order;
    }

    //邏輯處裡在service處裡
    @Transactional //復原資料庫操作,兩張資料庫數據同時發生或同時不發生 類似ROLL BACK,避免數據不一致
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
         //檢查user是否存在
        User user=userDao.getUserById(userId);

        if(user== null){
            log.warn("該userId{} 不存在",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount=0;
        //創建list
        List<OrderItem> orderItemList=new ArrayList<>();
         // for loop 前端使用人所購買每項商品
         for (BuyItem buyItem:createOrderRequest.getBuyItemList()){
          //根據前端傳入getProductId()去Product資料庫查詢數據,順便注入productDao bean
         Product product= productDao.getProductById(buyItem.getProductId());

          //檢查product是否存在,庫存是否足夠
             if(product==null){
                 log.warn("商品{} 不存在" ,buyItem.getProductId());
                 throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);

             } else if (product.getStock()< buyItem.getQuantity()) {
                 log.warn("商品{} 庫存數量不足,無法購買,庫存剩餘{},欲購買數量{}",
                         buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
             }
             //扣除商品庫存
             productDao.updateStock(product.getProductId(),product.getStock()-buyItem.getQuantity());


             //計算產品總價錢
             //計算user購買數量及價格,並計算品項價錢
             int amount=buyItem.getQuantity()*product.getPrice();
             //加總每項產品購買數量總價
             totalAmount=totalAmount+amount;
             /*串聯 orderId 數據訂單附加在那個產品上 並提供productId購買數量,計算該品項的花費?
             當插入數據到oreder_item表,須提供數據給Dao層
               */
             //轉換BuyItem 成 orderItem
             OrderItem orderItem=new OrderItem();
             orderItem.setProductId(buyItem.getProductId());
             orderItem.setQuantity(buyItem.getQuantity());
             orderItem.setAmount(amount);
             //傳遞orderItemList到dao層插入數據
             orderItemList.add(orderItem);
         }


        //創建訂單
        // 訂單是由2張table建立的故須分別在不同table插入數據
       Integer orderId= orderDao.createOrder(userId,totalAmount); // order table 建立數據一筆,將訂單資訊傳遞到createOrder()

       orderDao.createOrderItems(orderId,orderItemList); //orderItem table 也建立一筆訂單,拜託dao插入多筆數據到資料庫,先到OrderDao 再到OrderDaoimpl實作
        return orderId;
    }
}
