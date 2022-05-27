package com.study.redis.Service;

public interface IRedisService {

    void set(String key, String value);

    void get(String key);

    boolean expire(String key, long expire);

    void remove(String key);

    Long increment(String key,long delta);
}
