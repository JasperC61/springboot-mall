package com.jasper.springbootmall.service.impl;

import com.jasper.springbootmall.dao.UserDao;
import com.jasper.springbootmall.dto.UserLoginRequest;
import com.jasper.springbootmall.dto.UserRegisterRequest;
import com.jasper.springbootmall.model.User;
import com.jasper.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
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
       if(user !=null) {
           log.warn("該email{}已被使用", userRegisterRequest.getEmail());
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
       }
      // use MD5 gererate hashcode
           String hasedPassword= DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
       //save it to database
          userRegisterRequest.setPassword(hasedPassword);

       //建立帳號
        return userDao.createUser(userRegisterRequest);
    }
    //檢查前端輸入密碼與資料庫一致?
    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user=userDao.getUserByEmail(userLoginRequest.getEmail());
        //checked user exist ?
            if(user==null){
                log.warn("該email {} 尚未註冊",userLoginRequest.getEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        // use MD5 gererate hashcode

        String hasedPassword= DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
         // check password value  identical?

            if(user.getPassword().equals(hasedPassword)){
                return  user;
            }else {
                log.warn("該email {} 密碼不正確",userLoginRequest.getEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            }

    }
}
