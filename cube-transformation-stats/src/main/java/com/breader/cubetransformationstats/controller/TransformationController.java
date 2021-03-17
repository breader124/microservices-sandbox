package com.breader.cubetransformationstats.controller;

import com.breader.cubetransformationstats.model.Transformation;
import com.breader.cubetransformationstats.repository.TransformationRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Slf4j
public class TransformationController {

    private final TransformationRepository repo;

    @GetMapping("/transformations")
    @RateLimiter(name = "statistics-rate-limiter", fallbackMethod = "fallbackMethod")
    public List<EntityModel<Transformation>> getAllTransformations() {
        List<EntityModel<Transformation>> entityModelList = new ArrayList<>();
        List<Transformation> transformationList = repo.findAll();
        transformationList.forEach(t -> {
            WebMvcLinkBuilder link = linkTo(methodOn(TransformationController.class).getTransformation(t.getId()));
            entityModelList.add(EntityModel.of(t, link.withRel("transformation")));
        });
        return entityModelList;
    }

    @GetMapping("/transformations/{id}")
    public EntityModel<Transformation> getTransformation(@PathVariable Long id) {
        return repo.findById(id)
                .map(t -> {
                    WebMvcLinkBuilder link = linkTo(methodOn(TransformationController.class).getAllTransformations());
                    return EntityModel.of(t, link.withRel("all-transformations"));
                })
                .orElseThrow(NoSuchElementException::new);
    }

    @PostMapping("/transformations")
    public ResponseEntity<Transformation> pushNewTransformation(@RequestBody Transformation t) {
        Transformation savedOne = repo.save(t);
        ServletUriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        URI createdResourceUri = uriBuilder
                .path("/{id}")
                .build(savedOne.getId());
        return ResponseEntity.created(createdResourceUri).build();
    }

    public List<EntityModel<Transformation>> fallbackMethod(Exception t) throws Exception {
        log.error("Exception has been thrown:", t);
        throw t;
    }
}
