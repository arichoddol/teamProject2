package org.spring.backendspring.admin.repository;

import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminCrewRepository extends JpaRepository<CrewEntity, Long>{
    
}
