package com.jasper.springbootmall.controller;

import com.jasper.springbootmall.constant.ProductCategory;
import com.jasper.springbootmall.dto.ProductRequest;
import com.jasper.springbootmall.model.Product;
import com.jasper.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public  ResponseEntity<List<Product>> getProducts(
            //category required = false 參數式可選的
           @RequestParam(required = false) ProductCategory category,
           @RequestParam(required = false) String search
    ){
       List<Product> productList= productService.getProducts(category,search);
       return ResponseEntity.status(HttpStatus.OK).body(productList);
    }
    //察詢商品
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
    //新增商品
    @PostMapping("/products")
    public  ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Integer productId=  productService.createProduct(productRequest);
        Product product=productService.getProductById(productId);
        return  ResponseEntity.status(HttpStatus.CREATED).body(product);

    }
    //更新商品
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

    //刪除商品
    @DeleteMapping("/products/{productId}")
    public  ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
       productService.deleteProductById(productId);
       //204 return to front side
       return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();


    }


}
