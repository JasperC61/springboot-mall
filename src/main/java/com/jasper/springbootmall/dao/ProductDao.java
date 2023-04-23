package com.jasper.springbootmall.dao;

import com.jasper.springbootmall.dto.ProductRequest;
import com.jasper.springbootmall.model.Product;
import org.springframework.stereotype.Component;


public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);
}
