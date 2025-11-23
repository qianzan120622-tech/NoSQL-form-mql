package com.forestry.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "system_logs")
public class SystemLog {
    @Id
    private String id;
    private String module;
    private String operation;
    private String userId;
    private Map<String, Object> details;
    private LocalDateTime timestamp;
}