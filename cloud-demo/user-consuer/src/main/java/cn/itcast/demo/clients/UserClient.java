package cn.itcast.demo.clients;

import cn.itcast.demo.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("user/hello/{id}")
    ResponseEntity<User> hello(@PathVariable("id") Long id);

    //feign请求走的还是http请求
    //1,由ribbon进行负载均衡处理，获取到服务的ip:port
    //2,进行地址的拼接http://127.0.0.1:8081/user/hello/3
    //3，发起请求，获取结果，web-mvc中的消息转换器，把请求获取到的json，转为对象
    //4,实现用的动态代理
}
