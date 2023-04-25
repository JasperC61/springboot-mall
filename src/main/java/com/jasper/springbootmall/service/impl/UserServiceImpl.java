package com.jasper.springbootmall.service.impl;

import com.jasper.springbootmall.dao.UserDao;
import com.jasper.springbootmall.dto.UserRegisterRequest;
import com.jasper.springbootmall.model.User;
import com.jasper.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}
