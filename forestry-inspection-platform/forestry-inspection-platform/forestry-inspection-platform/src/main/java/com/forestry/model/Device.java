package com.forestry.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 设备信息   MongoDB
 */
@Data
@Document(collection = "devices")
public class Device {
    @Id
    private String id;
    private String deviceId;
    private String name;
    private String type;
    //型号
    private String model;
    private String status;
    //技术参数
    private Map<String, Object> technicalParams;
    //维护记录
    private List<MaintenanceRecord> maintenanceRecords;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

/**
 * 内嵌文档存储维护历史
 */
@Data
class MaintenanceRecord {
    //维护时间
    private LocalDateTime maintenanceDate;
    private String type;
    //描述
    private String description;
    //技术人员
    private String technician;
    //费用
    private Double cost;
}