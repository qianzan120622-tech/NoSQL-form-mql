package com.forestry.repository;

import com.forestry.model.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * 实时状态监控模块
 */
@Repository
public class DeviceStatusRepository {
    private static final String DEVICE_STATUS_KEY = "device:status:";
    private static final String DEVICE_LOCATION_KEY = "device:location:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void updateDeviceStatus(String deviceId, Map<String, Object> status) {
        String key = DEVICE_STATUS_KEY + deviceId;
        redisTemplate.opsForHash().putAll(key, status);
        //使用EXPIRE命令设置状态信息的自动过期
        redisTemplate.expire(key, Duration.ofMinutes(5));
    }

    public Map<Object, Object> getDeviceStatus(String deviceId) {
        String key = DEVICE_STATUS_KEY + deviceId;
        return redisTemplate.opsForHash().entries(key);
    }

    public void updateDeviceLocation(String deviceId, Coordinate location) {
        String key = DEVICE_LOCATION_KEY + deviceId;
        redisTemplate.opsForValue().set(key, location, Duration.ofMinutes(5));
    }

    public Coordinate getDeviceLocation(String deviceId) {
        String key = DEVICE_LOCATION_KEY + deviceId;
        return (Coordinate) redisTemplate.opsForValue().get(key);
    }
}
