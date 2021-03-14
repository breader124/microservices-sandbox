package com.breader.cubetransformationstats.controller;

import com.breader.cubetransformationstats.model.Transformation;
import com.breader.cubetransformationstats.repository.TransformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class TransformationController {

    private final TransformationRepository repo;

    @GetMapping("/transformations")
    public ResponseEntity<List<Transformation>> getAllTransformations() {
        List<Transformation> transformationList = repo.findAll();
        return ResponseEntity.ok(transformationList);
    }

    @GetMapping("/transformations/{id}")
    public ResponseEntity<Transformation> getTransformation(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(NoSuchElementException::new);
    }

    @PostMapping("/transformations")
    public ResponseEntity<Transformation> pushNewTransformation(@RequestBody Transformation t) {
        Transformation savedOne = repo.save(t);
        return ResponseEntity.ok(savedOne);
    }
}
