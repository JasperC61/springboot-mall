package com.jasper.springbootmall.service.impl;

import com.jasper.springbootmall.dao.UserDao;
import com.jasper.springbootmall.dto.UserRegisterRequest;
import com.jasper.springbootmall.model.User;
import com.jasper.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private  final static Logger log= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //檢查註冊email
       User user=  userDao.getUserByEmail(userRegisterRequest.getEmail());
       if(user !=null){
           log.warn("該email{}已被使用",userRegisterRequest.getEmail());
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

       }

       //建立帳號
        return userDao.createUser(userRegisterRequest);
    }
}
