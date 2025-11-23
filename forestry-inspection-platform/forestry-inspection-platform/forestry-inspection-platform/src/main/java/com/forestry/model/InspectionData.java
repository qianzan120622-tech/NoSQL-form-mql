package com.forestry.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 多源数据采集与存储模块  MongoDB + GridFS
 */
@Data
@Document(collection = "inspection_data")
public class InspectionData {
    @Id
    private String id;
    private String taskId;
    private String deviceId;
    private String dataType;
    //关联GridFS中的大文件
    private String fileId;
    //存储数据的元信息
    private Map<String, Object> metadata;
    //传感器读数
    private Map<String, Object> sensorReadings;
    //坐标信息
    private Coordinate location;
    private LocalDateTime timestamp;
}