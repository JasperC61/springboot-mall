package com.jasper.springbootmall.dto;

import jakarta.validation.constraints.NotNull;

public class BuyItem {
    @NotNull
    private  Integer productId;
    @NotNull
    private  Integer quantiy;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantiy() {
        return quantiy;
    }

    public void setQuantiy(Integer quantiy) {
        this.quantiy = quantiy;
    }
}
