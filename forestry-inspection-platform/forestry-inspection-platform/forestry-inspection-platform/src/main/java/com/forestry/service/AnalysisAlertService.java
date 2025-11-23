package com.forestry.service;

import com.forestry.model.AnalysisResult;
import com.forestry.repository.AnalysisResultRepository;
import com.forestry.repository.CacheRepository;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mongodb.BasicDBObject;

/**
 * 智能分析预警业务逻辑
 */
@Service
public class AnalysisAlertService {
    @Autowired
    private AnalysisResultRepository analysisResultRepository;

    @Autowired
    private CacheRepository cacheRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String ALERT_PUB_SUB_CHANNEL = "analysis:alerts:channel";

    // 1. 存储分析结果
    public AnalysisResult storeAnalysisResult(AnalysisResult result) {
        result.setAnalysisTime(LocalDateTime.now());
        return analysisResultRepository.save(result);
    }

    /**
     * 使用Redis的发布订阅模式推送实时预警
     * @param alertType
     * @param message
     */
    public void pushRealTimeAlert(String alertType, String message) {
        //构建预警消息数据（包含类型、内容、时间戳）
        Map<String, Object> alert = Map.of(
                "type", alertType,
                "message", message,
                "timestamp", System.currentTimeMillis()
        );
        //存入 Redis 队列
        cacheRepository.pushToQueue("alerts:queue", alert);
        //向指定频道（ALERT_PUB_SUB_CHANNEL）发布预警消息
        cacheRepository.publishToChannel(ALERT_PUB_SUB_CHANNEL, alert);
    }

    // 3. 查询最近实时预警（
    public List<Map<String, Object>> getRecentAlerts(int limit) {
        return cacheRepository.getFromQueue("alerts:queue", 0, limit - 1);
    }

    // 4. 查询分析历史
    public List<AnalysisResult> getAnalysisHistory(String taskId) {
        return analysisResultRepository.findByTaskId(taskId);
    }

    public void updateAlertStatus(String alertId, String status) {
        // 1. LocalDateTime 转为 String（格式：yyyy-MM-dd HH:mm:ss），Redis 可正常序列化
        String updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 2. 用 HashMap 构建参数
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("status", status);
        statusMap.put("updateTime", updateTime);

        cacheRepository.cacheUserSession("alert:" + alertId, statusMap);
    }

    //  按分析类型查询
    public List<AnalysisResult> getAnalysisByType(String analysisType) {
        return analysisResultRepository.findByAnalysisType(analysisType);
    }

    // 7. 按预警级别查询（
    public List<AnalysisResult> getAnalysisByAlertLevel(String alertLevel) {
        return analysisResultRepository.findByAlertLevel(alertLevel);
    }

    // 8. 按分析类型统计预警数量
    public List<Map<String, Object>> statsByAnalysisType() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("analysisType")
                        .count().as("alertCount")
                        .first("analysisType").as("analysisType")
        );

        AggregationResults<Map<String, Object>> results = mongoTemplate.aggregate(
                aggregation,
                "analysis_results",
                (Class<Map<String, Object>>) (Class<?>) Map.class
        );
        return results.getMappedResults();
    }

}