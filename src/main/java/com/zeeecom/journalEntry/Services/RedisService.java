package com.zeeecom.journalEntry.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    private RedisTemplate redisTemplate;

    public RedisService(RedisTemplate redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper(); //This is to change JSON Strings to Java Object
            return mapper.readValue(o.toString(), entityClass); //Mapping object to pojo so it can be converted to the class
        } catch (Exception e) {
            log.error("An error Occurred", e);
            return null;
        }
    }

    public void set(String key,Object o,long ttl) {
        try {
            ObjectMapper mapper=new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,jsonValue,ttl, TimeUnit.MINUTES); //ttl if set to -1 then no expiry
        } catch (Exception e) {
            log.error("An error Occurred", e);
        }
    }


}
