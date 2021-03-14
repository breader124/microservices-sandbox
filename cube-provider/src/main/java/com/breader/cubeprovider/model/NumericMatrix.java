package com.breader.cubeprovider.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class NumericMatrix {
    private final List<List<Double>> internal;

    public NumericMatrix(List<List<Double>> internalRepresentation) {
        this.internal = internalRepresentation;
    }

    public List<Double> multiplyByVector(List<Double> vector) {
        List<Double> result = new ArrayList<>();
        for (List<Double> row : internal) {
            double sum = IntStream
                    .range(0, vector.size())
                    .mapToDouble(i -> row.get(i) * vector.get(i))
                    .sum();
            result.add(sum);
        }
        return result;
    }

    @Override
    public String toString() {
        return internal.toString();
    }
}
