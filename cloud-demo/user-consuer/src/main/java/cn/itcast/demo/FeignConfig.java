package cn.itcast.demo;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
        //声明feign的日志输出内容
        //NONE 不输出默认
        //FULL 输出其所有的日志信息
    }
}
