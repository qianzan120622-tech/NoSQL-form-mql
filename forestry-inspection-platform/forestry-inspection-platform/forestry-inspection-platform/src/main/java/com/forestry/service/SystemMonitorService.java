package com.forestry.service;

import com.forestry.model.SystemLog;
import com.forestry.repository.SystemLogRepository;
import com.forestry.repository.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 系统监控业务逻辑
 */
@Service
public class SystemMonitorService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private CacheRepository cacheRepository;

    public SystemLog recordOperationLog(SystemLog log) {
        log.setTimestamp(LocalDateTime.now());
        return systemLogRepository.save(log);
    }

    public void updateSystemMetrics(String metricName, Object value) {
        // 1. LocalDateTime 转为 String（格式：yyyy-MM-dd HH:mm:ss）
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 2. 存储指标（键名统一加 system: 前缀，方便管理）
        String metricKey = "system:metrics:" + metricName;
        cacheRepository.cacheUserSession(metricKey, Map.of("value", value, "timestamp", timestamp));
    }

    public void incrementCounter(String counterName) {
        String key = "system:counter:" + counterName;
        // 读取值时不强制类型，用 Number 接收，再转为 long
        Number countNum = (Number) cacheRepository.getCachedResult(key);
        long count = (countNum == null) ? 0L : countNum.longValue(); // 兼容 Integer/Long
        cacheRepository.cacheQueryResult(key, count + 1, 1, java.util.concurrent.TimeUnit.HOURS);
    }

    public List<SystemLog> getLogsByModule(String module) {
        return systemLogRepository.findByModule(module);
    }

    public List<SystemLog> getLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return systemLogRepository.findByTimestampBetween(start, end);
    }

    public void checkThresholdAndAlert(String metricName, double threshold) {
        String metricKey = "system:metrics:" + metricName;

        // 1. 用Hash结构读取指标（和存储时一致）
        Map<Object, Object> metricData = cacheRepository.getUserSession(metricKey);
        if (metricData == null || metricData.isEmpty()) {
            System.out.println("阈值检测失败：指标 " + metricName + " 不存在");
            return;
        }

        // 2. 提取value并转换类型
        Object valueObj = metricData.get("value");
        if (valueObj == null) {
            System.out.println("阈值检测失败：指标 " + metricName + " 无value字段");
            return;
        }

        double currentValue;
        try {
            // 兼容字符串/数值类型的value（比如存储的是"75.5"或75.5）
            currentValue = Double.parseDouble(valueObj.toString());
        } catch (NumberFormatException e) {
            System.out.println("阈值检测失败：指标 " + metricName + " 的值不是数值类型：" + valueObj);
            return;
        }

        // 3. 阈值判断并推送告警
        if (currentValue > threshold) {
            String alertQueueKey = "system:alerts:threshold"; // 统一键名前缀
            Map<String, Object> alert = Map.of(
                    "alertType","阈值已经超出上限",
                    "metric", metricName,
                    "currentValue", currentValue,
                    "threshold", threshold,
                    "alertTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            cacheRepository.pushToQueue(alertQueueKey, alert);
            System.out.println("阈值告警触发：" + metricName + "=" + currentValue + " > " + threshold);
        } else {
            System.out.println("阈值未触发：" + metricName + "=" + currentValue + " ≤ " + threshold);
        }
    }
}