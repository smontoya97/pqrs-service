package com.pqrs.infrastructure.adapter.out.persistence.jpa;

import com.pqrs.infrastructure.adapter.out.persistence.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequestJpaRepository extends JpaRepository<RequestEntity, UUID> {
    List<RequestEntity> findByDependency(String dependency);
}
