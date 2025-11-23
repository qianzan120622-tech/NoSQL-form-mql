package com.forestry.repository;

import com.forestry.model.AnalysisResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  MongoDB分析结果访问
 */
@Repository
public interface AnalysisResultRepository extends MongoRepository<AnalysisResult, String> {
    List<AnalysisResult> findByTaskId(String taskId);
    List<AnalysisResult> findByAnalysisType(String analysisType);
    List<AnalysisResult> findByAlertLevel(String alertLevel);

}