//package cn.itcast.springboot.Controller;
//
//
//import cn.itcast.springboot.JdbcProperties;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class HelloController {
//
//    @Autowired
//    private JdbcProperties jdbcProperties;
//
//    @GetMapping("/hello")
//    public String hello(){
//        System.out.println(jdbcProperties);
//        return "hello springboot!";
//    }
//}
