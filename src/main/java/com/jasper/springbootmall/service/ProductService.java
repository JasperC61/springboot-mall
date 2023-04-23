package com.jasper.springbootmall.service;

import com.jasper.springbootmall.dto.ProductRequest;
import com.jasper.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);


    void updateProduct(Integer productId,ProductRequest productRequest);

}
