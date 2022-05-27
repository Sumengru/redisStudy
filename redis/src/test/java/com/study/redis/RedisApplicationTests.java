package com.study.redis;

import com.study.redis.Controller.RedisLockTestController;
import lombok.extern.slf4j.Slf4j;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
class RedisApplicationTests {

    @Autowired
    private RedisLockTestController LockServiceImpl;

    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();

    @Test
    @PerfTest(invocations = 100,threads = 10)
    public void buy(){
        LockServiceImpl.buy();
        log.info("buying");
}
}
