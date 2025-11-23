package com.forestry.service;

import com.forestry.model.InspectionTask;
import com.forestry.repository.InspectionTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 巡检任务业务
 */
@Service
public class InspectionTaskService {
    @Autowired
    private InspectionTaskRepository taskRepository;

    public InspectionTask createTask(InspectionTask task) {
        task.setCreateTime(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public boolean deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public InspectionTask updateTaskStatus(String id, String status, Map<String, Object> results) {
        Optional<InspectionTask> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            InspectionTask task = existingTask.get();
            task.setStatus(status);
            if (results != null) task.setResults(results);
            if ("COMPLETED".equals(status)) task.setEndTime(LocalDateTime.now());
            return taskRepository.save(task);
        }
        return null;
    }

    public List<InspectionTask> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<InspectionTask> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public List<InspectionTask> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    public List<InspectionTask> getTasksByDevice(String deviceId) {
        return taskRepository.findByDeviceIdsContaining(deviceId);
    }

    public List<InspectionTask> getTasksByTimeRange(LocalDateTime start, LocalDateTime end) {
        return taskRepository.findByCreateTimeBetween(start, end);
    }
}