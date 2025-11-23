package com.forestry.controller;

import com.forestry.model.Coordinate;
import com.forestry.service.MonitoringService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 *实时状态监控模块
 */
@RestController
@RequestMapping("/api/monitoring")
@Api(tags = "实时状态监控模块")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    /**
     * 根据deviceId更新设备状态
     * @param deviceId
     * @param status
     * @return
     */
    @PutMapping("/{deviceId}/status")
    @ApiOperation("根据deviceId更新设备状态")
    public ResponseEntity<Void> updateDeviceStatus(
            @PathVariable String deviceId,
            @RequestBody Map<String, Object> status) {
        monitoringService.updateDeviceStatus(deviceId, status);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据deviceId查询设备状态
     * @param deviceId
     * @return
     */
    @GetMapping("/{deviceId}/status")
    @ApiOperation("根据deviceId查询设备状态")
    public ResponseEntity<Map<Object, Object>> getDeviceStatus(@PathVariable String deviceId) {
        return ResponseEntity.ok(monitoringService.getDeviceStatus(deviceId));
    }

    /**
     * 根据deviceId更新设备实时位置
     * @param deviceId
     * @param location
     * @return
     */
    @PutMapping("/{deviceId}/location")
    @ApiOperation("根据deviceId更新设备实时位置")
    public ResponseEntity<Void> updateDeviceLocation(
            @PathVariable String deviceId,
            @RequestBody Coordinate location) {
        monitoringService.updateDevicePosition(deviceId, location);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据deviceId查询设备实时位置
     * @param deviceId
     * @return
     */
    @GetMapping("/{deviceId}/location")
    @ApiOperation("根据deviceId查询设备实时位置")
    public ResponseEntity<Coordinate> getDeviceLocation(@PathVariable String deviceId) {
        return ResponseEntity.ok(monitoringService.getDeviceLocation(deviceId));
    }

}