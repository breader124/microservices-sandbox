package com.breader.cubeprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {
    private String name;
    private double xPos;
    private double yPos;
    private double zPos;
}
