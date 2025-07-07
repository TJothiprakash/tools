package org.jp.tools.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisTestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/redis/test")
    public String test() {
        redisTemplate.opsForValue().set("ping", "pong", 10, TimeUnit.SECONDS);
        return redisTemplate.opsForValue().get("ping");
    }
}
