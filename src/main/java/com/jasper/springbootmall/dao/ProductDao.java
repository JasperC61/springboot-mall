package com.jasper.springbootmall.dao;

import com.jasper.springbootmall.model.Product;
import org.springframework.stereotype.Component;


public interface ProductDao {

    Product getProductById(Integer productId);
}
