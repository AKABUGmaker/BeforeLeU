package cn.itcast.demo.controller;

import cn.itcast.demo.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
    @HystrixCommand(fallbackMethod = "queryUserByIdFallBack")
    public User queryUserById(@PathVariable("id")Long id){

//        //根据服务id获取服务实例集合
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
//
//        ServiceInstance serviceInstance = instances.get(0);
//
//        //主机地址
//        String host = serviceInstance.getHost();
//
//        int port = serviceInstance.getPort();

        //测试熔断
        if (1==id){
            throw new RuntimeException("自定义一个异常");
        }
        /**
         * 请求服务地址时,需要将user-service转换为ip+sort
         * 获取ip和sort需要前往注册表,如果部署了集群
         * 需要通过*ribbon*来选取user-service的地址,
         * 实现负载均衡
         */
        String url = "http://user-service/user/hello/" + id;

        User user = restTemplate.getForObject(url,User.class);


        return user;

    }

    /**
     * 回调方法的返回值类型，和参数类型，必须保持一致
     * @param id
     * @return
     */
    public User queryUserByIdFallBack(@PathVariable("id")Long id){

        User user = new User();
        user.setId(id);
        user.setNote("小姐姐说，明天再来接待你哦");

        return user;

    }

}
