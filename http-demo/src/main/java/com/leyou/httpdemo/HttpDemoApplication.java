package com.leyou.httpdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HttpDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpDemoApplication.class, args);
	}

	//如果整个项目中只是用一次,不用声明成组件
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
