package cn.itcast.springboot.service;

import cn.itcast.springboot.mapper.UserMapper;
import cn.itcast.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User queryById(Long id) {
        User user = userMapper.queryById(id);

        return user;
    }
}
