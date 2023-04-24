package com.jasper.springbootmall.controller;

import com.jasper.springbootmall.constant.ProductCategory;
import com.jasper.springbootmall.dto.ProductQueryParams;
import com.jasper.springbootmall.dto.ProductRequest;
import com.jasper.springbootmall.model.Product;
import com.jasper.springbootmall.service.ProductService;
import com.jasper.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
public class ProductController {


    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public  ResponseEntity<Page<Product>> getProducts(
            //查詢條件 category required = false 參數式可選的
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            //排序 sorting
            @RequestParam (defaultValue = "created_date")String orderBy,
            @RequestParam (defaultValue = "desc")String sort,
            //分頁 pagination
            @RequestParam (defaultValue = "5")@Max(1000) @Min(0) Integer limit,
            @RequestParam (defaultValue = "0")@Min(0) Integer offset


    ){
        ProductQueryParams productQueryParams=new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);
        //商品列表 productList
        List<Product> productList= productService.getProducts(productQueryParams);
        //計算商品總筆數
        Integer total=productService.countProduct(productQueryParams);
        //分頁
        Page<Product> page =new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);
        //回傳前端
        return ResponseEntity.status(HttpStatus.OK).body(page);
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