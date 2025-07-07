package org.jp.tools.redis.resetpassword;

import org.jp.tools.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ResetPasswordService {

    private static final long TOKEN_TTL_MINUTES = 15;

    @Autowired
    private RedisService redisService;

    public void storeResetToken(String token, String email) {
        String key = "reset_token:" + token;
        redisService.set(key, email, TOKEN_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public String getEmailByToken(String token) {
        return redisService.get("reset_token:" + token);
    }

    public void invalidateToken(String token) {
        redisService.delete("reset_token:" + token);
    }
}
