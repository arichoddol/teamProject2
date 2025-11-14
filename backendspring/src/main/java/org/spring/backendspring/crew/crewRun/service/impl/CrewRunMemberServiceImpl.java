package org.spring.backendspring.crew.crewRun.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewRun.dto.CrewRunMemberDto;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunMemberEntity;
import org.spring.backendspring.crew.crewRun.repository.CrewRunMemberRepository;
import org.spring.backendspring.crew.crewRun.repository.CrewRunRepository;
import org.spring.backendspring.crew.crewRun.service.CrewRunMemberService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewRunMemberServiceImpl implements CrewRunMemberService {
    private final CrewRunMemberRepository crewRunMemberRepository;
    private final CrewRunRepository crewRunRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<CrewRunMemberDto> findCrewRunMemberList(Long crewId, Long runId) {
        crewRunRepository.findByCrewEntityIdAndId(crewId,runId)
                .orElseThrow(()-> new NullPointerException("해당 런닝스케줄이 없음"));
        return crewRunMemberRepository.findAllByCrewRunEntityId(runId)
                .stream().map(CrewRunMemberDto::toCrewRunMemberDto).collect(Collectors.toList());
    }

    @Override
    public void insertCrewMemberRun(Long runId, Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(()-> new NullPointerException("회원아닌데 어케함"));
        CrewRunEntity crewRunEntity = crewRunRepository.findById(runId).orElseThrow(()-> new NullPointerException("런닝스케줄없는데 어케함"));
        Optional<CrewRunMemberEntity> opCrewRunMember = crewRunMemberRepository.findByMemberEntityId(memberId);
        if (opCrewRunMember.isPresent()) {
            throw new IllegalArgumentException("이미 런닝에 참가함");
        }
        
        crewRunMemberRepository.save(CrewRunMemberEntity.insertCrewRun(CrewRunMemberEntity.builder()
                        .crewRunEntity(crewRunEntity)
                        .memberEntity(memberEntity)
                .build()));
    }

    @Override
    public void deleteCrewMemberRun(Long runId, Long memberId) {
        crewRunMemberRepository.findByCrewRunEntityIdAndMemberEntityId(runId,memberId)
                        .orElseThrow(()-> new NullPointerException("해당 크루 런닝스케줄이 없음"));

        crewRunMemberRepository.deleteByCrewRunEntityIdAndMemberEntityId(runId,memberId);
    }
}
