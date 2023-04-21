package com.jasper.springbootmall.rowmapper;

import com.jasper.springbootmall.constant.ProductCategory;
import com.jasper.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int i) throws SQLException {
        Product product=new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));

        // String ->Enum
        String categoryStr=rs.getString("category");
        ProductCategory category=ProductCategory.valueOf(categoryStr);
        product.setCategory(category);
        // from daabase String transfer to Enum
        //product.setCategory(ProductCategory.valueOf(rs.getString("category")));



        product.setImageUrl(rs.getString("image_url"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setDescription(rs.getString("description"));
        product.setCreatedDate(rs.getTimestamp("created_date"));
        product.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
          return  product;
    }
}
