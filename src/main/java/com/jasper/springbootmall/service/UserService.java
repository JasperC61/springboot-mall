package com.jasper.springbootmall.service;

import com.jasper.springbootmall.dto.UserRegisterRequest;
import com.jasper.springbootmall.model.User;

public interface UserService {

    User getUserById(Integer userId);
    Integer register(UserRegisterRequest userRegisterRequest);
}
