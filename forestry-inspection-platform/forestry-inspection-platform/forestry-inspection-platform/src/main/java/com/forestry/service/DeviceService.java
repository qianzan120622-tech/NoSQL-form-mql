package com.forestry.service;

import com.forestry.model.Device;
import com.forestry.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *  设备管理业务逻辑
 */
@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public Device addDevice(Device device) {
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());
        return deviceRepository.save(device);
    }

    public boolean deleteDevice(String id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Device updateDevice(String id, Device device) {
        Optional<Device> existingDevice = deviceRepository.findById(id);
        if (existingDevice.isPresent()) {
            Device existing = existingDevice.get();
            if (device.getName() != null) existing.setName(device.getName());
            if (device.getType() != null) existing.setType(device.getType());
            if (device.getModel() != null) existing.setModel(device.getModel());
            if (device.getStatus() != null) existing.setStatus(device.getStatus());
            if (device.getTechnicalParams() != null) existing.setTechnicalParams(device.getTechnicalParams());
            if (device.getMaintenanceRecords() != null) existing.setMaintenanceRecords(device.getMaintenanceRecords());
            existing.setUpdateTime(LocalDateTime.now());
            return deviceRepository.save(existing);
        }
        return null;
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(String id) {
        return deviceRepository.findById(id);
    }

    public List<Device> getDevicesByStatus(String status) {
        return deviceRepository.findByStatus(status);
    }

    public List<Device> getDevicesByType(String type) {
        return deviceRepository.findByType(type);
    }
}