package com.breader.cubeprovider.controller;

import com.breader.cubeprovider.model.Cube;
import com.breader.cubeprovider.model.Transformation;
import com.breader.cubeprovider.proxy.TransformationsStatisticsProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cube")
@RequiredArgsConstructor
public class CubeRotationController {
    private final Cube cube;
    private final TransformationsStatisticsProxy statisticsProxy;

    @GetMapping("/current")
    public String currentCubePos() {
        return cube.asString();
    }

    @GetMapping("/rotateX/{rad}")
    public String xRotatedCube(@PathVariable double rad) {
        cube.rotateX(rad);
        statisticsProxy.pushNewTransformation(new Transformation("x", rad));
        return cube.asString();
    }

    @GetMapping("/rotateY/{rad}")
    public String yRotatedCube(@PathVariable double rad) {
        cube.rotateY(rad);
        statisticsProxy.pushNewTransformation(new Transformation("y", rad));
        return cube.asString();
    }

    @GetMapping("/rotateZ/{rad}")
    public String zRotatedCube(@PathVariable double rad) {
        cube.rotateZ(rad);
        statisticsProxy.pushNewTransformation(new Transformation("z", rad));
        return cube.asString();
    }

    @GetMapping("/rotateX/{xRad}/rotateY/{yRad}/rotateZ/{zRad}")
    public String rotatedCube(@PathVariable double xRad, @PathVariable double yRad, @PathVariable double zRad) {
        cube.rotateX(xRad);
        statisticsProxy.pushNewTransformation(new Transformation("x", xRad));
        cube.rotateY(yRad);
        statisticsProxy.pushNewTransformation(new Transformation("y", yRad));
        cube.rotateZ(zRad);
        statisticsProxy.pushNewTransformation(new Transformation("z", zRad));
        return cube.asString();
    }

}
