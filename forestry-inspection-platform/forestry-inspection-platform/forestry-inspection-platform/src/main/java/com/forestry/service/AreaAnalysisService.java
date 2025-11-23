package com.forestry.service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 林区分析业务逻辑
 */
@Service
public class AreaAnalysisService {
    @Autowired
    private Driver neo4jDriver;

    /**
     * 创建林区节点
     * @param areaId
     * @param name
     * @param type
     * @param areaSize
     * @param coordinates
     */
    public void createForestArea(String areaId, String name, String type, Double areaSize, List<Double> coordinates) {
        try (Session session = neo4jDriver.session()) {
            // 用MERGE确保节点不存在时创建，存在时不重复创建
            String query = "MERGE (a:ForestArea {areaId: $areaId}) " +
                    "SET a.name = $name, " +
                    "    a.type = $type, " +
                    "    a.areaSize = $areaSize, " +
                    "    a.coordinates = $coordinates";
            session.run(query, Values.parameters(
                    "areaId", areaId,
                    "name", name,
                    "type", type,
                    "areaSize", areaSize,
                    "coordinates", coordinates
            ));
        }
    }

    /**
     * 创建巡检点，并关联到林区（HAS_INSPECTION_POINT 关系）
     */
    public void createInspectionPoint(
            String areaId, String pointId, String name,
            Double longitude, Double latitude, String pointType, Integer priority) {
        try (Session session = neo4jDriver.session()) {
            // 1. 先创建巡检点节点（MERGE 避免重复）
            // 2. 同时建立与林区的关联关系（HAS_INSPECTION_POINT）
            String cypher = "MATCH (area:ForestArea {areaId: $areaId}) " + // 找到目标林区
                    "MERGE (point:InspectionPoint {pointId: $pointId}) " + // 创建/匹配巡检点
                    "SET point.name = $name, " +
                    "    point.longitude = $longitude, " +
                    "    point.latitude = $latitude, " +
                    "    point.pointType = $pointType, " +
                    "    point.priority = $priority " +
                    "MERGE (area)-[:HAS_INSPECTION_POINT]->(point)"; // 建立关联关系
            session.run(cypher, Values.parameters(
                    "areaId", areaId,
                    "pointId", pointId,
                    "name", name,
                    "longitude", longitude,
                    "latitude", latitude,
                    "pointType", pointType,
                    "priority", priority
            ));
        }
    }

    public void createAreaTopology(String areaId1, String areaId2, double distance, String terrainType) {
        try (Session session = neo4jDriver.session()) {
            String query = "MATCH (a1:ForestArea {areaId: $areaId1}), (a2:ForestArea {areaId: $areaId2}) " +
                    "CREATE (a1)-[:ADJACENT_TO {distance: $distance, terrainType: $terrainType}]->(a2)";
            session.run(query, Values.parameters("areaId1", areaId1, "areaId2", areaId2, "distance", distance, "terrainType", terrainType));
        }
    }

    public void updateRelationship(String areaId1, String areaId2, double newDistance) {
        try (Session session = neo4jDriver.session()) {
            String query = "MATCH (a1:ForestArea {areaId: $areaId1})-[r:ADJACENT_TO]-(a2:ForestArea {areaId: $areaId2}) " +
                    "SET r.distance = $newDistance";
            session.run(query, Values.parameters("areaId1", areaId1, "areaId2", areaId2, "newDistance", newDistance));
        }
    }

    public List<Map<String, Object>> findShortestPath(String startAreaId, String endAreaId) {
        try (Session session = neo4jDriver.session()) {
            String query = "MATCH (start:ForestArea {areaId: $startAreaId}), " +
                    "(end:ForestArea {areaId: $endAreaId}), " +
                    "path = shortestPath((start)-[:ADJACENT_TO*]-(end)) " +
                    "RETURN [node in nodes(path) | node.name] as pathNodes, " +
                    "length(path) as hopCount, " +
                    "reduce(total = 0, r in relationships(path) | total + r.distance) as totalDistance";

            return session.run(query, Values.parameters("startAreaId", startAreaId, "endAreaId", endAreaId))
                    .list(record -> {
                        Map<String, Object> result = new HashMap<>();
                        result.put("pathNodes", record.get("pathNodes").asList());
                        result.put("hopCount", record.get("hopCount").asInt());
                        result.put("totalDistance", record.get("totalDistance").asDouble());
                        return result;
                    });
        }
    }

    public List<Map<String, Object>> analyzePropagation(String startAreaId, int maxHops) {
        try (Session session = neo4jDriver.session()) {
            String query = "MATCH (start:ForestArea {areaId: $startAreaId}) " +
                    "MATCH path = (start)-[:ADJACENT_TO*1.." + maxHops + "]-(connected) " +
                    "RETURN collect(DISTINCT connected.areaId) as connectedAreas, " +
                    "count(DISTINCT connected) as totalConnected";

            return session.run(query, Values.parameters("startAreaId", startAreaId))
                    .list(record -> {
                        Map<String, Object> result = new HashMap<>();
                        result.put("connectedAreas", record.get("connectedAreas").asList());
                        result.put("totalConnected", record.get("totalConnected").asInt());
                        return result;
                    });
        }
    }
}