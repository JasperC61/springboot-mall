package com.jasper.springbootmall.dao;

import com.jasper.springbootmall.dto.UserRegisterRequest;
import com.jasper.springbootmall.model.User;

public interface UserDao {

    User getUserById(Integer userId);

    Integer createUser(UserRegisterRequest userRegisterRequest);

}
