package com.forestry.controller;

import com.forestry.model.InspectionTask;
import com.forestry.service.InspectionTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 巡检任务管理模块
 */
@RestController
@RequestMapping("/api/tasks")
@Api(tags = "巡检任务管理模块")
public class InspectionTaskController {

    @Autowired
    private InspectionTaskService taskService;

    /**
     * 新增巡检任务
     * @param task
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增巡检任务")
    public ResponseEntity<InspectionTask> createTask(@RequestBody InspectionTask task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    /**
     * 根据id删除任务
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除任务")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        boolean deleted = taskService.deleteTask(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * 更新任务状态
     * @param id
     * @param status
     * @param results
     * @return
     */
    @PutMapping("/{id}/status")
    @ApiOperation("更新任务状态")
    public ResponseEntity<InspectionTask> updateTaskStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestBody(required = false) Map<String, Object> results) {
        InspectionTask updatedTask = taskService.updateTaskStatus(id, status, results);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    /**
     * 查询所有任务
     * @return
     */
    @GetMapping
    @ApiOperation("查询所有任务")
    public ResponseEntity<List<InspectionTask>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * 根据任务id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据任务id查询")
    public ResponseEntity<InspectionTask> getTaskById(@PathVariable String id) {
        Optional<InspectionTask> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据任务状态查询
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    @ApiOperation("根据任务状态查询")
    public ResponseEntity<List<InspectionTask>> getTasksByStatus(@PathVariable String status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    /**
     * 根据分配的设备查询
     * @param deviceId
     * @return
     */
    @GetMapping("/device/{deviceId}")
    @ApiOperation("根据分配的设备查询")
    public ResponseEntity<List<InspectionTask>> getTasksByDevice(@PathVariable String deviceId) {
        return ResponseEntity.ok(taskService.getTasksByDevice(deviceId));
    }

    /**
     * 根据时间范围查询
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/time-range")
    @ApiOperation("根据时间范围查询")
    public ResponseEntity<List<InspectionTask>> getTasksByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(taskService.getTasksByTimeRange(start, end));
    }
}