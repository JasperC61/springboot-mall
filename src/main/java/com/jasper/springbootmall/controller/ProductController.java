package com.jasper.springbootmall.controller;

import com.jasper.springbootmall.dto.ProductRequest;
import com.jasper.springbootmall.model.Product;
import com.jasper.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/products")
    public  ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Integer productId=  productService.createProduct(productRequest);
        Product product=productService.getProductById(productId);
        return  ResponseEntity.status(HttpStatus.CREATED).body(product);

    }

    @PutMapping("/products/{productId}")
    public  ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                  @RequestBody @Valid ProductRequest productRequest){
       //檢查商品是否存在
        Product product=productService.getProductById(productId);
       //將資料返回前端
        if (product==null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //修改商品的數據
        productService.updateProduct(productId,productRequest);

        Product updatedProduct=productService.getProductById(productId);

        return  ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
}
