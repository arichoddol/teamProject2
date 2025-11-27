package org.spring.backendspring.crew.crewRun.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.crew.crewRun.dto.CrewRunDto;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.crew.crewRun.repository.CrewRunRepository;
import org.spring.backendspring.crew.crewRun.service.CrewRunService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewRunServiceImpl implements CrewRunService {
    private final CrewRunRepository crewRunRepository;
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;


    @Override
    public List<CrewRunDto> findCrewRunList(Long crewId) {

        return crewRunRepository.findAllByCrewEntityId(crewId).stream().map(CrewRunDto::toCrewRunDto).collect(Collectors.toList());
//
    }

    @Override
    public CrewRunDto detailCrewRun(Long crewId, Long runId) {
        return crewRunRepository.findByCrewEntityIdAndId(crewId,runId).map(CrewRunDto::toCrewRunDto)
                .orElseThrow(()-> new IllegalArgumentException("해당 스케줄이 없음"));


    }

    @Override
    public CrewRunDto crewRunCreate(CrewRunDto runDto) {
        Long crewId = runDto.getCrewId();
        Long memberId = runDto.getMemberId();

        //회원 맞냐?
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 아닌데 어떻게 함?"));

        //크루 있냐?
        CrewEntity crewEntity =crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("크루가 없는데 어떻게 함?"));

        crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("크루원이 아닌데 어떻게 함?"));

        CrewRunEntity crewRunEntity = crewRunRepository.save(CrewRunEntity.createCrewRun(CrewRunDto.builder()
                        .title(runDto.getTitle())
                        .startAt(runDto.getStartAt())
                        .endAt(runDto.getEndAt())
                        .place(runDto.getPlace())
                        .routeHint(runDto.getRouteHint())
                        .crewEntity(crewEntity)
                        .memberEntity(memberEntity)
                .build()));


        return CrewRunDto.toCrewRunDto(crewRunEntity);
    }

    @Override
    public CrewRunDto crewRunUpdate(CrewRunDto runDto) {

        Long crewId = runDto.getCrewId();
        Long memberId = runDto.getMemberId();
        Long runId = runDto.getId();

        crewRunRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("스케줄이 없는데 어떻게 함?"));

        //회원 맞냐?
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 아닌데 어떻게 함?"));

        //크루 있냐?
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("크루가 없는데 어떻게 함?"));

        crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("크루원이 아닌데 어떻게 함?"));


        CrewRunEntity crewRunUpdateEntity = crewRunRepository.save(CrewRunEntity.updateCrewRun(CrewRunDto.builder()
                .id(runId)
                .title(runDto.getTitle())
                .startAt(runDto.getStartAt())
                .endAt(runDto.getEndAt())
                .place(runDto.getPlace())
                .routeHint(runDto.getRouteHint())
                .crewEntity(crewEntity)
                .memberEntity(memberEntity)
                .build()));


        return CrewRunDto.toCrewRunDto(crewRunUpdateEntity);
    }
    @Override
    public void crewRunDelete(Long crewId, Long runId) {
        crewRunRepository.deleteByCrewEntityIdAndId(crewId, runId);
    }


}
