package com.study.redis.Service.Impl;


import com.study.redis.Service.IRedisService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value) {
        try{
            stringRedisTemplate.opsForValue().set(key, value);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void get(String key) {
        try{
            stringRedisTemplate.opsForValue().get(key);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean expire(String key, long expire) {
        try{
            return Boolean.TRUE.equals(stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS));
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void remove(String key) {
        try{
            stringRedisTemplate.delete(key);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Long increment(String key, long delta) {
        if(delta < 0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key,delta);
    }
}
