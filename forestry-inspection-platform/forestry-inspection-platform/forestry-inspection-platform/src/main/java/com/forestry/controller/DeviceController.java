package com.forestry.controller;

import com.forestry.model.Device;
import com.forestry.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 设备信息管理
 */
@RestController
@RequestMapping("/api/devices")
@Api(tags = "设备信息管理")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     *新增设备
     * @param device
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增设备")
    public ResponseEntity<Device> addDevice(@RequestBody Device device) {
        Device savedDevice = deviceService.addDevice(device);
        return ResponseEntity.ok(savedDevice);
    }

    /**
     * 删除设备
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除设备")
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        boolean deleted = deviceService.deleteDevice(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * 更新设备
     * @param id
     * @param device
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("更新设备")
    public ResponseEntity<Device> updateDevice(@PathVariable String id, @RequestBody Device device) {
        Device updatedDevice = deviceService.updateDevice(id, device);
        return updatedDevice != null ? ResponseEntity.ok(updatedDevice) : ResponseEntity.notFound().build();
    }

    /**
     * 查询所有设备信息
     * @return
     */
    @GetMapping
    @ApiOperation("查询所有设备信息")
    public ResponseEntity<List<Device>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    /**
     * 按照id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("按照id查询设备信息")
    public ResponseEntity<Device> getDeviceById(@PathVariable String id) {
        Optional<Device> device = deviceService.getDeviceById(id);
        return device.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * 按照设备状态查询
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    @ApiOperation("按照设备状态查询")
    public ResponseEntity<List<Device>> getDevicesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(deviceService.getDevicesByStatus(status));
    }

    /**
     * 按照设备类型查询
     * @param type
     * @return
     */
    @GetMapping("/type/{type}")
    @ApiOperation("按照设备类型查询")
    public ResponseEntity<List<Device>> getDevicesByType(@PathVariable String type) {
        return ResponseEntity.ok(deviceService.getDevicesByType(type));
    }
}