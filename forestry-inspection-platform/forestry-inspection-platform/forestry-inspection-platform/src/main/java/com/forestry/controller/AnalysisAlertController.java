package com.forestry.controller;

import com.forestry.model.AnalysisResult;
import com.forestry.service.AnalysisAlertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 智能分析与预警
 */
@RestController
@RequestMapping("/api/analysis-alerts")
@Api(tags = "智能分析与预警")
public class AnalysisAlertController {

    @Autowired
    private AnalysisAlertService analysisAlertService;

    /**
     *存储智能分析结果
     * @param result
     * @return
     */
    @PostMapping("/results")
    @ApiOperation("存储智能分析结果")
    public ResponseEntity<AnalysisResult> storeAnalysisResult(@RequestBody AnalysisResult result) {
        return ResponseEntity.ok(analysisAlertService.storeAnalysisResult(result));
    }

    /**
     * 推送分类预警  subscribe analysis:alerts:channel订阅
     * @param alertType
     * @param message
     * @return
     */
    @PostMapping("/alerts")
    @ApiOperation("推送分类预警")
    public ResponseEntity<Void> pushRealTimeAlert(
            @RequestParam String alertType,
            @RequestParam String message) {
        analysisAlertService.pushRealTimeAlert(alertType, message);
        return ResponseEntity.ok().build();
    }

    /**
     * 按任务 ID 查询分析历史
     * @param taskId
     * @return
     */
    @GetMapping("/history/{taskId}")
    @ApiOperation("按任务 ID 查询分析历史")
    public ResponseEntity<List<AnalysisResult>> getAnalysisHistory(@PathVariable String taskId) {
        return ResponseEntity.ok(analysisAlertService.getAnalysisHistory(taskId));
    }

    /**
     * 更新预警状态（Redis HSET）
     * @param alertId
     * @param status
     * @return
     */
    @PutMapping("/alerts/{alertId}/status")
    @ApiOperation("更新预警状态")
    public ResponseEntity<Void> updateAlertStatus(
            @PathVariable String alertId,
            @RequestParam String status) {
        analysisAlertService.updateAlertStatus(alertId, status);
        return ResponseEntity.ok().build();
    }

    /**
     * 按分析类型统计预警数量（聚合查询）
     * @return
     */
    @GetMapping("/stats/by-type")
    @ApiOperation("按分析类型统计预警数量")
    public ResponseEntity<List<Map<String, Object>>> statsByAnalysisType() {
        return ResponseEntity.ok(analysisAlertService.statsByAnalysisType());
    }

    /**
     * 根据分析类型查询
     * @param analysisType
     * @return
     */
    @GetMapping("/type/{analysisType}")
    @ApiOperation("根据分析类型查询")
    public ResponseEntity<List<AnalysisResult>> getAnalysisByType(@PathVariable String analysisType) {
        return ResponseEntity.ok(analysisAlertService.getAnalysisByType(analysisType));
    }

    /**
     * 根据预警级别查询
     * @param alertLevel
     * @return
     */
    @GetMapping("/alert-level/{alertLevel}")
    @ApiOperation("根据预警级别查询")
    public ResponseEntity<List<AnalysisResult>> getAnalysisByAlertLevel(@PathVariable String alertLevel) {
        return ResponseEntity.ok(analysisAlertService.getAnalysisByAlertLevel(alertLevel));
    }
}