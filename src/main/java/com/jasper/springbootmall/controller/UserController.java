package com.jasper.springbootmall.controller;


import com.jasper.springbootmall.dto.UserRegisterRequest;
import com.jasper.springbootmall.model.User;
import com.jasper.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        //接收前端傳入參數放入userService中register方法,在資料庫中創建userRegisterRequest生成userId
          Integer userId=userService.register(userRegisterRequest);
          //根據userId去查詢該筆資料出來
          User user=userService.getUserById(userId);
          //將商品數據取回傳給前端
          return  ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
