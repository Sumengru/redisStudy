package com.study.redis.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
@Slf4j
@Service
public class LockServiceImpl {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY = "book";

    private static final Long STOCK = 50L;

    public String buy(){
        String buyBefore = stringRedisTemplate.opsForValue().get(KEY);
        if(Objects.isNull(buyBefore)){
            log.error("未找到\"{}\"的库存信息~！",KEY);
            return "暂未上架~";
        }
        long buybeforL = Long.parseLong(buyBefore);
        if(buybeforL > 0){
            Long buyAfter = stringRedisTemplate.opsForValue().decrement((KEY));
            log.info("剩余图书==={}",buyAfter);
            return "购买成功~";
        }
        else {
            log.info("库存不足！");
            return "库存不足~";
        }
    }
}
