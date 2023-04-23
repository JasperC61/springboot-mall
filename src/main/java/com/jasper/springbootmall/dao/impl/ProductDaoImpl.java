package com.jasper.springbootmall.dao.impl;

import com.jasper.springbootmall.dao.ProductDao;
import com.jasper.springbootmall.dto.ProductQueryParams;
import com.jasper.springbootmall.dto.ProductRequest;
import com.jasper.springbootmall.model.Product;
import com.jasper.springbootmall.rowmapper.ProductRowMapper;
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
public class ProductDaoImpl  implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql="select product_id ,product_name,category,image_url, price,stock, description,created_date, last_modified_date from product where 1=1";
        Map<String,Object> map=new HashMap<>();
        if(productQueryParams.getCategory() !=null){
            sql=sql+" and category=:category";
            map.put("category",productQueryParams.getCategory().name());
        }
        if(productQueryParams.getSearch() !=null){
            sql=sql+" and product_name   like :search";
            map.put("search","%"+ productQueryParams.getSearch()+ "%");
        }

        List<Product> productList=namedParameterJdbcTemplate.query(sql,map,new ProductRowMapper());

        return productList;
    }

    @Override
    public Product getProductById(Integer productId) {

       String sql="select product_id ,product_name,category,image_url, price,stock, description,created_date, last_modified_date from product where product_id=:productId";
        //String sql="select product_id,product_name from product where product_id= :productId";
        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        if(productList.size()>0){
            return productList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql="update product set product_name=:productName,category=:category,image_url=:imageUrl,"+
                "price=:price,stock=:stock,description=:description,last_modified_date=:lastModifiedDate"+
                " where product_id=:productId";
        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        map.put("lastModifiedDate",new Date());
        //執行sql,修改商品數據
        namedParameterJdbcTemplate.update(sql,map);

    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql="delete from product where product_Id=:productId";
        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);
        namedParameterJdbcTemplate.update(sql,map);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql="insert into product(product_name,category,image_url,price,stock,description,created_date,last_modified_date) values (:productName,:category,:imageUrl,:price,:stock,:description,:createdDate,:lastModifiedDate)";
        //reveive the front site key in data put on map

        Map<String,Object> map=new HashMap<>();
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        //new date ,record current time
        Date now=new Date();
        // get the time as the createdDate and lastModifiedDate, put on map
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);
        // use KeyHolder storge the productId
        KeyHolder keyHolder=new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);
        int productId=keyHolder.getKey().intValue();

        return productId;


    }
}
