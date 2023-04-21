package com.jasper.springbootmall.controller;

import com.jasper.springbootmall.model.Product;
import com.jasper.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
   @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
         Product product=productService.getProductById(productId);

         if (product !=null){
             //200 ok,response the body data to front site
             return  ResponseEntity.status(HttpStatus.OK).body(product);

         }else{
             //null no data 404,response the body data to front site
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
         }
    }
}
