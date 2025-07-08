package org.jp.tools.redis.resetpassword;

import org.jp.tools.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ResetPasswordService {

    private static final long TOKEN_TTL_MINUTES = 15;

    private final RedisTemplate<String, String> redisTemplate;

    public ResetPasswordService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    private RedisService redisService;

    public void storeResetToken(String token, String email) {
        String key = "reset_token:" + token;
        redisTemplate.opsForValue().set(token, email, 15, TimeUnit.MINUTES);
        System.out.println("Saved in Redis: " + token + " -> " + email);
        redisService.set(key, email, TOKEN_TTL_MINUTES, TimeUnit.MINUTES);
        System.out.println("Redis PING = " + redisTemplate.getConnectionFactory().getConnection().ping());
        System.out.println("key stored in redis = " + key);
    }

    public String getEmailByToken(String token) {
        return redisService.get("reset_token:" + token);
    }

    public void invalidateToken(String token) {
        redisService.delete("reset_token:" + token);
    }
}
