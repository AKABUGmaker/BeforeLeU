package com.leyou.httpdemo;

import com.leyou.httpdemo.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HttpDemoApplication.class)
public class HttpDemoApplicationTests {

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void httpGet() {
		//先根据指定地址发起web请求,获取到json,然后消息转换工具,从json转为user对象
		User user = this.restTemplate.getForObject("http://localhost:8081/user/hello/1", User.class);
		System.out.println(user);
	}

}
