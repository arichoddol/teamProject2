package org.spring.backendspring.crew.crew.repository;

import java.util.List;
import java.util.Optional;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<CrewEntity, Long> {

    Optional<Object> findByIdAndCrewMemberEntities_MemberEntity_Id(Long crewId, Long loginUserId);

}
