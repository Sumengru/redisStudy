package com.study.redis.Controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("redis")
public class RedisLockTestController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY = "book";

    private static final Long STOCK = 50L;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("init")
    public String init(){
        stringRedisTemplate.opsForValue().set(KEY,String.valueOf(STOCK));
        return "初始化成功~";
    }

    @GetMapping("buy")
    public String buy(){
        String buyBefore = stringRedisTemplate.opsForValue().get(KEY);
        if(Objects.isNull(buyBefore)){
            log.error("未找到\"{}\"的库存信息~！",KEY);
            return "暂未上架~";
        }
        long buybeforL = Long.parseLong(buyBefore);
        if(buybeforL > 0){
            Long buyAfter = stringRedisTemplate.opsForValue().decrement((KEY));
            log.info("剩余图书{}",buyAfter);
            return "购买成功~";
        }
        else {
            log.info("库存不足！");
            return "库存不足~";
        }
    }

    @GetMapping("buyWithLock")
    public String buy1() {
        RLock lock = null;
        try {
            lock = redissonClient.getLock("lock");
            if(lock.tryLock(3, TimeUnit.SECONDS)) {
                RAtomicLong buyBefore = redissonClient.getAtomicLong(KEY);
                if(Objects.isNull(buyBefore)) {
                    log.error("未找到\"{}\"的库存信息~", KEY);
                    return "暂未上架～";
                }
                long buyBeforeL = buyBefore.get();
                if(buyBeforeL > 0) {
                    Long buyAfter = buyBefore.decrementAndGet();
                    log.info("剩余图书==={}", buyAfter);
                    return "购买成功～";
                }
                else {
                    log.info("库存不足～");
                    return "库存不足～";
                }
            }
            else {
                log.error("获取锁失败～");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            //如果当前线程保持锁定则解锁
            if(null != lock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return "系统错误～";
    }
}
