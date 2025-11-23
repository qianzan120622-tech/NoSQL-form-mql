package com.forestry.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis缓存管理模块
 */
@Repository
public class CacheRepository {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //缓存查询结果（键值对存储，带过期时间）
    public void cacheQueryResult(String key, Object result, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, result, timeout, unit);
    }

    // 获取缓存的查询结果
    public Object getCachedResult(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    //缓存用户会话（哈希存储）
    public void cacheUserSession(String sessionId, Map<String, Object> sessionData) {
        redisTemplate.opsForHash().putAll("session:" + sessionId, sessionData);
        redisTemplate.expire("session:" + sessionId, Duration.ofHours(2));
    }

    // 获取用户会话
    public Map<Object, Object> getUserSession(String sessionId) {
        return redisTemplate.opsForHash().entries("session:" + sessionId);
    }

    // 推送数据到队列（左进）
    public void pushToQueue(String queueName, Object data) {
        redisTemplate.opsForList().leftPush(queueName, data);
        // 队列只保留最近100条，避免数据积压
        redisTemplate.opsForList().trim(queueName, 0, 99);
    }

    // 从队列弹出数据（右出）
    public Object popFromQueue(String queueName) {
        return redisTemplate.opsForList().rightPop(queueName);
    }

    // 从队列查询最近N条数据（支持前端拉取实时预警）
    public List<Map<String, Object>> getFromQueue(String queueName, long start, long end) {
        // range(start, end)：查询队列中从start到end的元素（0为最左侧，即最新数据）
        return redisTemplate.opsForList().range(queueName, start, end)
                .stream()
                .map(obj -> (Map<String, Object>) obj) // 转换为预警数据格式
                .collect(Collectors.toList());
    }

    /**
     *     Redis发布订阅 - 发布消息到指定频道
     */
    public void publishToChannel(String channelName, Object message) {
        redisTemplate.convertAndSend(channelName, message);
    }

    // 删除缓存
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }
}