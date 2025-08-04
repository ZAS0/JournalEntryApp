package com.zeeecom.journalEntry.Services;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @Disabled
    public void testRedis(){
        redisTemplate.opsForValue().set("email","zaika@biryani.com");
        Object email = redisTemplate.opsForValue().get("email");

        Object salary = redisTemplate.opsForValue().get("salary");
        int a=10;
    }

}
