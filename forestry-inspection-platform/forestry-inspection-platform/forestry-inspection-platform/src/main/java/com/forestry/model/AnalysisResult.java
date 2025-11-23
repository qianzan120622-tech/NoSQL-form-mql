package com.forestry.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 智能分析与预警 MongoDB
 */
@Data
@Document(collection = "analysis_results")
public class AnalysisResult {
    @Id
    private String id;
    private String taskId;
    //区分不同类型的分析（健康评估、病虫害识别等
    private String analysisType;
    //分析结果
    private Map<String, Object> results;
    // 支持预警分级管理
    private String alertLevel;
    private LocalDateTime analysisTime;
}