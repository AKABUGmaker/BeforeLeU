package cn.itcast.demo.controller;

import cn.itcast.demo.clients.UserClient;
import cn.itcast.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("heima86")
public class UserNewController {

    @Autowired
    private UserClient userClient;

    @GetMapping("{id}")
    public ResponseEntity<User> hello(@PathVariable("id")Long id){

        ResponseEntity<User> user = userClient.hello(id);

        return user;
    }
}
