package com.forestry.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

/**
 * 巡检点管理 Neo4j
 */
@Data
//存储: Neo4j
@Node("InspectionPoint")
public class InspectionPoint {
    @Id
    @GeneratedValue
    private Long id;
    private String pointId;
    private String name;
    private Double longitude;
    private Double latitude;
    private String pointType;
    //支持巡检路径优化
    private Integer priority;
}