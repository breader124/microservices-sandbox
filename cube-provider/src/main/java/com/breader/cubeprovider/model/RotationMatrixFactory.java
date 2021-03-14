package com.breader.cubeprovider.model;

import java.util.ArrayList;
import java.util.List;

public class RotationMatrixFactory {
    public static NumericMatrix getInstance(String type, double angle) {
        String typeLowercase = type.toLowerCase();
        List<List<Double>> matrix = getInitializedSpace();

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        switch (typeLowercase) {
            case "x":
                matrix.get(0).set(0, 1.0);
                matrix.get(1).set(1, cos);
                matrix.get(2).set(1, sin);
                matrix.get(1).set(2, -sin);
                matrix.get(2).set(2, cos);
                return new NumericMatrix(matrix);
            case "y":
                matrix.get(0).set(0, cos);
                matrix.get(0).set(2, sin);
                matrix.get(1).set(1, 1.0);
                matrix.get(2).set(0, -sin);
                matrix.get(2).set(2, cos);
                return new NumericMatrix(matrix);
            case "z":
                matrix.get(0).set(0, cos);
                matrix.get(1).set(1, cos);
                matrix.get(1).set(0, sin);
                matrix.get(0).set(1, -sin);
                matrix.get(2).set(2, 1.0);
                return new NumericMatrix(matrix);
            default:
                throw new IllegalArgumentException("No such axis: " + type);
        }
    }

    private static List<List<Double>> getInitializedSpace() {
        List<List<Double>> matrix = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < 3; j++) {
                matrix.get(i).add(0.0);
            }
        }
        return matrix;
    }
}
