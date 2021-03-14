package com.breader.cubeprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.Future;

@Data
@AllArgsConstructor
public class PointPosChange {
    private Point oldPoint;
    private Future<Point> newPoint;
}
