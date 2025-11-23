package com.forestry.controller;

import com.forestry.service.AreaAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 林区关系分析模块
 */
@RestController
@RequestMapping("/api/analysis/areas")
@Api(tags = "林区关系分析模块")
public class AreaAnalysisController {

    @Autowired
    private AreaAnalysisService areaAnalysisService;

    /**
     * 创建林区节点
     */
    @PostMapping("add")
    @ApiOperation("创建林区节点")
    public ResponseEntity<Void> createForestArea(
            @RequestParam String areaId,
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam Double areaSize,
            @RequestParam List<Double> coordinates) {
        areaAnalysisService.createForestArea(areaId, name, type, areaSize, coordinates);
        return ResponseEntity.ok().build();
    }

    /**
     * 创建巡检点并关联到指定林区（建立 HAS_INSPECTION_POINT 关系）
     */
    @PostMapping("/inspection-points")
    @ApiOperation(" 创建巡检点并关联到指定林区")
    public ResponseEntity<Void> createInspectionPoint(
            @RequestParam String areaId, // 所属林区ID
            @RequestParam String pointId, // 巡检点唯一标识
            @RequestParam String name, // 巡检点名称
            @RequestParam Double longitude, // 经度
            @RequestParam Double latitude, // 纬度
            @RequestParam String pointType, // 类型（如火情监测点）
            @RequestParam Integer priority) { // 优先级（1-高）
        areaAnalysisService.createInspectionPoint(areaId, pointId, name, longitude, latitude, pointType, priority);
        return ResponseEntity.ok().build();
    }

    /**
     * 创建林区拓扑关系
     * @param fromAreaId
     * @param toAreaId
     * @param distance
     * @param terrainType
     * @return
     */
    @PostMapping("/topology")
    @ApiOperation("创建林区拓扑关系")
    public ResponseEntity<Void> createTopology(
            @RequestParam String fromAreaId,
            @RequestParam String toAreaId,
            @RequestParam double distance,
            @RequestParam String terrainType) {
        areaAnalysisService.createAreaTopology(fromAreaId, toAreaId, distance, terrainType);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新林区关系
     * @param fromAreaId
     * @param toAreaId
     * @param newDistance
     * @return
     */
    @PutMapping("/relationships")
    @ApiOperation("更新林区关系")
    public ResponseEntity<Void> updateRelationship(
            @RequestParam String fromAreaId,
            @RequestParam String toAreaId,
            @RequestParam double newDistance) {
        areaAnalysisService.updateRelationship(fromAreaId, toAreaId, newDistance);
        return ResponseEntity.ok().build();
    }

    /**
     * 查找最短路径
     * @param startAreaId
     * @param endAreaId
     * @return
     */
    @GetMapping("/shortest-path")
    @ApiOperation("查找最短路径")
    public ResponseEntity<List<Map<String, Object>>> findShortestPath(
            @RequestParam String startAreaId,
            @RequestParam String endAreaId) {
        return ResponseEntity.ok(areaAnalysisService.findShortestPath(startAreaId, endAreaId));
    }


    /**
     * 从指定起始林区出发，分析异常（如病虫害、火情）
     * 在 maxHops（最大跳数）内的传播范围（基于林区间的 ADJACENT_TO 相邻关系）。
     * @param startAreaId
     * @param maxHops
     * @return
     */
    @GetMapping("/propagation")
    @ApiOperation("分析林区异常传播")
    public ResponseEntity<List<Map<String, Object>>> analyzePropagation(
            @RequestParam String startAreaId,
            @RequestParam int maxHops) {
        return ResponseEntity.ok(areaAnalysisService.analyzePropagation(startAreaId, maxHops));
    }
}