package com.forestry.repository;

import com.forestry.model.InspectionData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB巡检数据访问
 */
@Repository
public interface InspectionDataRepository extends MongoRepository<InspectionData, String> {
    List<InspectionData> findByTaskId(String taskId);
    List<InspectionData> findByDeviceId(String deviceId);
    List<InspectionData> findByDataType(String dataType);
    List<InspectionData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}