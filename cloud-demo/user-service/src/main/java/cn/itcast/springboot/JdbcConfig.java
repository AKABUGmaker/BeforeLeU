package cn.itcast.springboot;//package cn.itcast.springboot;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableConfigurationProperties(JdbcProperties.class)
//public class JdbcConfig {
//
//    @Autowired
//    JdbcProperties properties;
//
//    @Bean
//    public DataSource getDataSource(){
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUrl(properties.getUrl());
//        dataSource.setDriverClassName(properties.getDriverClassName());
//        dataSource.setUsername(properties.getUsername());
//        dataSource.setPassword(properties.getPassword());
//        return dataSource;
//    }
//}
