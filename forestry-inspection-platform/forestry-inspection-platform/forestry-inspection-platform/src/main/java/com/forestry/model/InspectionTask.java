package com.forestry.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 巡检任务管理  MongoDB
 */
@Data
@Document(collection = "inspection_tasks")
public class InspectionTask {
    @Id
    private String id;
    private String taskId;
    private String name;
    private String description;
    //任务分配的设备id
    private List<String> deviceIds;
    //坐标列表存储规划路径
    private List<Coordinate> plannedRoute;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    //任务结果数据
    private Map<String, Object> results;
}