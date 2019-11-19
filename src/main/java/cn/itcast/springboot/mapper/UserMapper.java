package cn.itcast.springboot.mapper;

import cn.itcast.springboot.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    public User queryById(@Param("id") Long id);
}
