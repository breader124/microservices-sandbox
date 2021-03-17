package com.breader.cubeprovider.proxy;

import com.breader.cubeprovider.model.Transformation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "cube-transformation-stats")
public interface TransformationsStatisticsProxy {
    @PostMapping(value = "/statistics/transformations")
    ResponseEntity<Transformation> pushNewTransformation(Transformation transformation);
}
