package com.forestry.service;

import com.forestry.model.Coordinate;
import com.forestry.repository.DeviceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 实时监控业务逻辑
 */
@Service
public class MonitoringService {
    @Autowired
    private DeviceStatusRepository deviceStatusRepository;

    public void updateDeviceStatus(String deviceId, Map<String, Object> status) {
        deviceStatusRepository.updateDeviceStatus(deviceId, status);
    }

    public Map<Object, Object> getDeviceStatus(String deviceId) {
        return deviceStatusRepository.getDeviceStatus(deviceId);
    }

    public void updateDevicePosition(String deviceId, Coordinate location) {
        deviceStatusRepository.updateDeviceLocation(deviceId, location);
    }

    public Coordinate getDeviceLocation(String deviceId) {
        return deviceStatusRepository.getDeviceLocation(deviceId);
    }

}