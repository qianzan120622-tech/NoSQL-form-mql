package com.forestry.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import java.util.List;
import java.util.Set;

/**
 *林区关系分析 Neo4j
 */
@Data
@Node("ForestArea")
public class ForestArea {
    @Id
    @GeneratedValue
    private Long id;
    private String areaId;
    private String name;
    private String type;
    private Double areaSize;
    // 多边形坐标支持空间计算
    private List<Double> coordinates;

    @Relationship(type = "ADJACENT_TO")
    //图关系支持路径规划和异常传播分析
    private Set<ForestArea> adjacentAreas;

    @Relationship(type = "HAS_INSPECTION_POINT")
    //建立林区与巡检点的关联
    private Set<InspectionPoint> inspectionPoints;
}