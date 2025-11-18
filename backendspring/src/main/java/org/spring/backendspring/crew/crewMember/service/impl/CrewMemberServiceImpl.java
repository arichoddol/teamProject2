package org.spring.backendspring.crew.crewMember.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.crew.crewMember.service.CrewMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewMemberServiceImpl implements CrewMemberService {

    private final CrewMemberRepository crewMemberRepository;
    @Override
    public List<CrewMemberDto> findCrewMemberList(Long crewId) {

        return crewMemberRepository.findAllByCrewEntityId(crewId).stream().map(CrewMemberDto::toCrewMember).collect(Collectors.toList());
    }

    @Override
    public void deleteCrewMember(Long crewMemberTbId) {
        CrewMemberEntity crewMemberEntity = crewMemberRepository.findById(crewMemberTbId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 크루원"));
        crewMemberRepository.delete(crewMemberEntity);
    }

    @Override
    public CrewMemberDto detailCrewMember(Long crewId, Long crewMemberId) {
        return crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId,crewMemberId)
                .map(CrewMemberDto::toCrewMember).orElseThrow(()->new IllegalArgumentException("크루원이 존재하지않음"));
    }

}