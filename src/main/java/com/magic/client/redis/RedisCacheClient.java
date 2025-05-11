package com.magic.client.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheClient {

  private final StringRedisTemplate redisTemplate;

  public RedisCacheClient(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }
  public void set(String key, String value, long timeout, TimeUnit unit) {
    redisTemplate.opsForValue().set(key, value, timeout, unit);
  }
  public void delete(String key) {
    redisTemplate.delete(key);
  }
  public boolean isUserTokenInWhiteList(String userId, String token) {
    String tokenFromRedis = get("whiteList:" + userId);
    return tokenFromRedis != null && tokenFromRedis.equals(token);
  }
}
