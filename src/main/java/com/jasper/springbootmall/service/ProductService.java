package com.jasper.springbootmall.service;

import com.jasper.springbootmall.dto.ProductRequest;
import com.jasper.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);


    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProductById(Integer productId);

}
