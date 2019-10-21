package com.learning.springboot.service;

import com.learning.springboot.mapper.UserMapper;
import com.learning.springboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user){
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if(dbUser == null){
            //创建新用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新用户信息
            user.setGmtModified(System.currentTimeMillis());
            userMapper.update(user);
        }
    }

}
