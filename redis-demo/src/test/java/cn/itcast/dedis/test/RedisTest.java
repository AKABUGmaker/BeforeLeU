package cn.itcast.dedis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisTest(){

        redisTemplate.opsForValue().set("heima86","18K",30, TimeUnit.SECONDS);
            //redisTemplate.opsForValue().set("heima","86");
        if (redisTemplate.hasKey("heima")){

            String value = redisTemplate.opsForValue().get("heima");
            System.out.println(value);

        }

    }

    @Test
    public void testHash(){

        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps("userId1");

        ops.put("goodsId4","goods4");

        if (ops.hasKey("goodsId3")){
            String value= ops.get("goodsId3");

            System.out.println("value = " + value);
        }
    }

    @Test
    public void testHashDel(){

        //判断key是否存在，如果存在则删除
        if (redisTemplate.hasKey("userId1")){
            redisTemplate.delete("userId1");
        }
    }

}
