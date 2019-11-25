package cn.itcast.demo.controller;

import cn.itcast.demo.pojo.User;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("consumer")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("{id}")
    public User queryUserById(@PathVariable("id")Long id){

        //根据服务id获取服务实例集合
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");

        ServiceInstance serviceInstance = instances.get(0);

        //主机地址
        String host = serviceInstance.getHost();

        int port = serviceInstance.getPort();


        User user = restTemplate.getForObject("http://"+host+":"+port+"/user/hello/" + id, User.class);


        return user;

    }

}
