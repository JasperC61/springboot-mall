package com.jasper.springbootmall.dao.impl;

import com.jasper.springbootmall.dao.OrderDao;
import com.jasper.springbootmall.dto.OrderQueryParams;
import com.jasper.springbootmall.model.Order;
import com.jasper.springbootmall.model.OrderItem;
import com.jasper.springbootmall.rowmapper.OrderItemRowMapper;
import com.jasper.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {
    @Autowired
     private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql="select count(*) from `order` where 1=1";
        Map<String,Object> map=new HashMap<>();
        //查詢條件
        sql=addFilteringSql(sql,map,orderQueryParams);

        Integer total=namedParameterJdbcTemplate.queryForObject(sql,map,Integer.class);

        return total;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql="select order_id, user_id,total_amount, created_date, last_modified_date from `order` where 1=1";
        Map<String,Object> map=new HashMap<>();
        //查詢條件
        sql=addFilteringSql(sql,map,orderQueryParams);
        //排序
        sql=sql+" order by created_date desc";
        //分頁
        sql=sql+" LIMIT :limit OFFSET :offset";
        map.put("limit",orderQueryParams.getLimit());
        map.put("offset",orderQueryParams.getOffset());

        List<Order> orderList=namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());

        return orderList;
    }

    private String addFilteringSql(String sql,Map<String,Object> map,OrderQueryParams orderQueryParams){
         if (orderQueryParams.getUserId()!=null){
             sql=sql+" and user_id=:userId";
             map.put("userId",orderQueryParams.getUserId());
         }
         return  sql;
    }

    @Override
    public List<OrderItem> getOrderItemByOrderId(Integer orderId) {
        String sql="select oi.order_item_id, oi.order_id ,oi.product_id, oi.quantity, oi.amount, p.product_name , p.image_url " +
                 " from order_item as oi " +
                " left join product as p on oi.product_id=p.product_id " +
                " where oi.order_id= :orderId ";
        Map<String,Object> map=new HashMap<>();
        map.put("orderId",orderId);
        List<OrderItem> orderItemList=namedParameterJdbcTemplate.query(sql,map,new OrderItemRowMapper());
        return orderItemList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql="select order_id , user_id , total_amount, created_date, last_modified_date from `order`  where order_id=:orderId";
        Map<String,Object> map=new HashMap<>();
        map.put("orderId",orderId);
        List<Order> orderList=namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());

        if(orderList.size()>0){
            return  orderList.get(0);

        }else {
            return null;
        }

    }

    //接收service層傳遞過來參數插入到資料庫中,實作OrderDao方法,dao單純跟資料庫作溝通
    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
       //order為SQL關鍵字,硬要用須加上‵才可用
        String sql="insert into `order`(user_id,total_amount,created_date,last_modified_date)"+
                "values (:userId, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String,Object> map=new HashMap<>();
        map.put("userId" ,userId);
        map.put("totalAmount",totalAmount);
        Date now=new Date();
        map.put("createdDate" ,now);
        map.put("lastModifiedDate",now);
        KeyHolder keyHolder =new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);
        int orderId=keyHolder.getKey().intValue();
         //在資料庫中生成orderId
         return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {

        //使用 for loop 一條一條sql 加數據,效率差
//        for(OrderItem orderItem:orderItemList) {
//            String sql = "insert into order_item(order_id,product_id,quantity,amount) values(:orderId, :productId, :quantity, :amount)";
//            Map<String, Object> map = new HashMap<>();
//            map.put("orderId", orderId);
//            map.put("productId", orderItem.getProductId());
//            map.put("quantity", orderItem.getQuantity());
//            map.put("amount", orderItem.getAmount());
//            namedParameterJdbcTemplate.update(sql, map);
//        }

            //使用 batchUpdate 一次性加入數據,效率高
            String sql="insert into order_item(order_id,product_id,quantity,amount)"+
                    "values(:orderId, :productId, :quantity, :amount)";

        MapSqlParameterSource[] parameterSources=new MapSqlParameterSource[orderItemList.size()];
        for (int i=0;i<orderItemList.size();i++){
            OrderItem orderItem=orderItemList.get(i);
            parameterSources[i]=new MapSqlParameterSource();
            parameterSources[i].addValue("orderId",orderId);
            parameterSources[i].addValue("productId",orderItem.getProductId());
            parameterSources[i].addValue("quantity",orderItem.getQuantity());
            parameterSources[i].addValue("amount",orderItem.getAmount());



        }
        namedParameterJdbcTemplate.batchUpdate(sql,parameterSources);
    }
}
