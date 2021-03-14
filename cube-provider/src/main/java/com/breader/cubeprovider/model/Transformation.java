package com.breader.cubeprovider.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transformation {
    private Long id;
    private final String rotationType;
    private final Double angle;

    public Transformation(String rotationType, Double angle) {
        this.rotationType = rotationType;
        this.angle = angle;
    }
}
