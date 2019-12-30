package com.automata.queues.redis;

import com.automata.queues.StorageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public class RedisResponseQueue implements StorageQueue {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private String queueKey = "ResponseQueue";

    public Object push(Object args) {
        return redisTemplate.opsForList().leftPush(queueKey, (String) args);
    }

    public Object pull(Object args) {
        return redisTemplate.opsForList().rightPop(queueKey);
    }

    @Override
    public Object blockPull(Long waitSeconds) {
        return redisTemplate.opsForList().rightPop(queueKey, waitSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Object blockPush(Long waitSeconds, Object element) {
        return redisTemplate.opsForList().leftPush(queueKey, (String) element);
    }
}
