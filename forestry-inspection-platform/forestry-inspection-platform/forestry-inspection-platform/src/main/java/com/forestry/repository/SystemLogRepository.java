package com.forestry.repository;

import com.forestry.model.SystemLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB系统日志访问
 */
@Repository
public interface SystemLogRepository extends MongoRepository<SystemLog, String> {
    List<SystemLog> findByModule(String module);
    List<SystemLog> findByOperation(String operation);
    List<SystemLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}