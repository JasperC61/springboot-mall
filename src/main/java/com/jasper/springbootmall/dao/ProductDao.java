package com.jasper.springbootmall.dao;

import com.jasper.springbootmall.dto.ProductRequest;
import com.jasper.springbootmall.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;


public interface ProductDao {
    List<Product> getProducts();
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
