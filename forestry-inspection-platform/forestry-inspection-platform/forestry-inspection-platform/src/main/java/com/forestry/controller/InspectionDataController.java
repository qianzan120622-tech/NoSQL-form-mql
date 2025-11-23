package com.forestry.controller;

import com.forestry.model.InspectionData;
import com.forestry.service.InspectionDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 多源数据采集与存储模块
 */
@RestController
@RequestMapping("/api/inspection-data")
@Api(tags = "多源数据采集与存储模块")
public class InspectionDataController {

    @Autowired
    private InspectionDataService dataService;

    /**
     * 新增巡检数据
     * @param data
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增巡检数据")
    public ResponseEntity<InspectionData> storeInspectionData(@RequestBody InspectionData data) {
        return ResponseEntity.ok(dataService.storeInspectionData(data));
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除巡检数据")
    public ResponseEntity<Void> deleteInspectionData(@PathVariable String id) {
        boolean deleted = dataService.deleteInspectionData(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * 更新数据标签
     * @param id
     * @param metadata
     * @return
     */
    @PutMapping("/{id}/tags")
    @ApiOperation("更新数据标签")
    public ResponseEntity<InspectionData> updateDataTags(
            @PathVariable String id,
            @RequestBody Map<String, Object> metadata) {
        InspectionData updatedData = dataService.updateDataTags(id, metadata);
        return updatedData != null ? ResponseEntity.ok(updatedData) : ResponseEntity.notFound().build();
    }

    /**
     * 根据taskId查询数据
     * @param taskId
     * @return
     */
    @GetMapping("/task/{taskId}")
    @ApiOperation("根据taskId查询数据")
    public ResponseEntity<List<InspectionData>> getDataByTask(@PathVariable String taskId) {
        return ResponseEntity.ok(dataService.getInspectionDataByTask(taskId));
    }

    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    @GetMapping("/device/{deviceId}")
    @ApiOperation("根据设备id查询数据")
    public ResponseEntity<List<InspectionData>> getDataByDevice(@PathVariable String deviceId) {
        return ResponseEntity.ok(dataService.getInspectionDataByDevice(deviceId));
    }

    /**
     * 根据数据类型查询
     * @param dataType
     * @return
     */
    @GetMapping("/type/{dataType}")
    @ApiOperation("根据数据类型查询")
    public ResponseEntity<List<InspectionData>> getDataByType(@PathVariable String dataType) {
        return ResponseEntity.ok(dataService.getInspectionDataByType(dataType));
    }

    /**
     * 根据时间范围查询
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/time-range")
    @ApiOperation("根据时间范围查询")
    public ResponseEntity<List<InspectionData>> getDataByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dataService.getInspectionDataByTimeRange(start, end));
    }
}