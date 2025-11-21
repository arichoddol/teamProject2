package org.spring.backendspring.API.marathonapi.repository;

import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarathonRepository extends JpaRepository<Marathon, Long> {
}
