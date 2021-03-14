package com.breader.cubetransformationstats.repository;

import com.breader.cubetransformationstats.model.Transformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransformationRepository extends JpaRepository<Transformation, Long> {
}
