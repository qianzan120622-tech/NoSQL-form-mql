package com.forestry.controller;

import com.forestry.model.SystemLog;
import com.forestry.service.SystemMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统监控与日志模块
 */
@RestController
@RequestMapping("/api/system-monitor")
@Api(tags = "系统监控与日志模块")
public class SystemMonitorController {

    @Autowired
    private SystemMonitorService systemMonitorService;

    /**
     * 记录操作日志
     * @param log
     * @return
     */
    @PostMapping("/logs")
    @ApiOperation("记录操作日志")
    public ResponseEntity<SystemLog> recordOperationLog(@RequestBody SystemLog log) {
        return ResponseEntity.ok(systemMonitorService.recordOperationLog(log));
    }

    /**
     * 更新系统指标
     * @param metricName
     * @param value
     * @return
     */
    @PutMapping("/metrics/{metricName}")
    @ApiOperation("更新系统指标")
    public ResponseEntity<Void> updateSystemMetrics(
            @PathVariable String metricName,
            @RequestBody Object value) {
        systemMonitorService.updateSystemMetrics(metricName, value);
        return ResponseEntity.ok().build();
    }

    /**
     * Redis 计数器（INCR 命令）统计各类业务 / 系统事件的发生次数
     * @param counterName
     * @return
     */
    @PostMapping("/counters/{counterName}/increment")
    @ApiOperation("计数器")
    public ResponseEntity<Void> incrementCounter(@PathVariable String counterName) {
        systemMonitorService.incrementCounter(counterName);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据模块查询日志
     * @param module
     * @return
     */
    @GetMapping("/logs/module/{module}")
    @ApiOperation("根据模块查询日志")
    public ResponseEntity<List<SystemLog>> getLogsByModule(@PathVariable String module) {
        return ResponseEntity.ok(systemMonitorService.getLogsByModule(module));
    }

    /**
     * 根据时间范围查询日志
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/logs/time-range")
    @ApiOperation("根据时间范围查询日志")
    public ResponseEntity<List<SystemLog>> getLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(systemMonitorService.getLogsByTimeRange(start, end));
    }

    /**
     * 阈值检测与告警
     * @param metricName
     * @param threshold
     * @return
     */
    @PostMapping("/threshold-check")
    @ApiOperation("阈值检测与告警")
    public ResponseEntity<Void> checkThresholdAndAlert(
            @RequestParam String metricName,
            @RequestParam double threshold) {
        systemMonitorService.checkThresholdAndAlert(metricName, threshold);
        return ResponseEntity.ok().build();
    }
}