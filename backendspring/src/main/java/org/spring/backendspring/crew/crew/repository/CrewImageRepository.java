package org.spring.backendspring.crew.crew.repository;

import java.util.List;

import org.spring.backendspring.crew.crew.entity.CrewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewImageRepository extends JpaRepository<CrewImageEntity, Long> {

    List<CrewImageEntity> findByCrewEntity_Id(Long id);
    
}
