package org.spring.backendspring.crew.crewCreate.repository;

import org.spring.backendspring.crew.crewCreate.entity.CrewCreateRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewCreateRequestRepository extends JpaRepository<CrewCreateRequestEntity, Long> {
    
}
