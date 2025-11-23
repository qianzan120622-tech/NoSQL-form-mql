package com.forestry.service;

import com.forestry.model.InspectionData;
import com.forestry.repository.InspectionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *  巡检数据业务逻辑
 */
@Service
public class InspectionDataService {
    @Autowired
    private InspectionDataRepository dataRepository;

    public InspectionData storeInspectionData(InspectionData data) {
        data.setTimestamp(LocalDateTime.now());
        return dataRepository.save(data);
    }

    public boolean deleteInspectionData(String id) {
        if (dataRepository.existsById(id)) {
            dataRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public InspectionData updateDataTags(String id, Map<String, Object> metadata) {
        Optional<InspectionData> existingData = dataRepository.findById(id);
        if (existingData.isPresent()) {
            InspectionData data = existingData.get();
            data.setMetadata(metadata);
            return dataRepository.save(data);
        }
        return null;
    }

    public List<InspectionData> getInspectionDataByTask(String taskId) {
        return dataRepository.findByTaskId(taskId);
    }

    public List<InspectionData> getInspectionDataByDevice(String deviceId) {
        return dataRepository.findByDeviceId(deviceId);
    }

    public List<InspectionData> getInspectionDataByType(String dataType) {
        return dataRepository.findByDataType(dataType);
    }

    public List<InspectionData> getInspectionDataByTimeRange(LocalDateTime start, LocalDateTime end) {
        return dataRepository.findByTimestampBetween(start, end);
    }
}