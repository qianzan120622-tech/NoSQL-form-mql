package com.forestry.repository;

import com.forestry.model.InspectionTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB任务数据访问
 */
@Repository
public interface InspectionTaskRepository extends MongoRepository<InspectionTask, String> {

    List<InspectionTask> findByStatus(String status);

    List<InspectionTask> findByDeviceIdsContaining(String deviceId);

    List<InspectionTask> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
}