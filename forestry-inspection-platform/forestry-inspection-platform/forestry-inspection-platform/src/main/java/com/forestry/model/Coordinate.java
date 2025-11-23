package com.forestry.model;

import lombok.Data;

/**
 * 坐标信息
 */
@Data
public class Coordinate {
    private Double longitude;
    private Double latitude;
    private Double altitude;
}