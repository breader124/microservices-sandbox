package com.breader.cubeprovider;

import com.breader.cubeprovider.model.Cube;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/cube")
@RequiredArgsConstructor
public class CubeRotationController {
    private final Cube cube;

    @GetMapping("/current")
    public String currentCubePos() {
        return cube.asJson();
    }

    @GetMapping("/rotateX/{rad}")
    public String xRotatedCube(@PathVariable double rad) {
        cube.rotateX(rad);
        return cube.asJson();
    }

    @GetMapping("/rotateY/{rad}")
    public String yRotatedCube(@PathVariable double rad) {
        cube.rotateY(rad);
        return cube.asJson();
    }

    @GetMapping("/rotateZ/{rad}")
    public String zRotatedCube(@PathVariable double rad) {
        cube.rotateZ(rad);
        return cube.asJson();
    }

    @GetMapping("/rotateX/{xRad}/rotateY/{yRad}/rotateZ/{zRad}")
    public String rotatedCube(@PathVariable double xRad, @PathVariable double yRad, @PathVariable double zRad) {
        cube.rotateX(xRad);
        cube.rotateY(yRad);
        cube.rotateZ(zRad);
        return cube.asJson();
    }

}
