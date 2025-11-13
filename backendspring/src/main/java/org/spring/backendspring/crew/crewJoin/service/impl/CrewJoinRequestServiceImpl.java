package org.spring.backendspring.crew.crewJoin.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.common.RequestStatus;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.spring.backendspring.crew.crewJoin.repository.CrewJoinRequestRepository;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewJoinRequestServiceImpl implements CrewJoinRequestService {

    private final CrewJoinRequestRepository crewJoinRequestRepository;
    private final MemberRepository memberRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRepository crewRepository;

    @Override
    public CrewJoinRequestDto crewJoinRequest(CrewJoinRequestDto joinDto) {
        Long crewId = joinDto.getCrewRequestId();
        Long memberId = joinDto.getMemberRequestId();
        //회원 맞냐?
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 아닌데 어떻게 가입신청함?"));

        //크루 있냐?
        CrewEntity crewEntity =crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("크루없는데 어떻게 해당 크루에 가입신청함?"));

        //이미 크루멤버냐?
        Optional<CrewMemberEntity> optionalCrewMember = crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId);
        if (optionalCrewMember.isPresent()) throw new IllegalArgumentException("이미 해당 크루 멤버임");

        //이미 신청했냐?
        Optional<CrewJoinRequestEntity> optionalCrewJoinRequestEntity = crewJoinRequestRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId);
                if (optionalCrewJoinRequestEntity.isPresent()) throw new IllegalArgumentException("이미 해당 크루 가입 요청 대기중임");



        CrewJoinRequestEntity crewJoinRequestEntity = crewJoinRequestRepository.save(CrewJoinRequestEntity.insertCrewJoinRequest(CrewJoinRequestDto.builder()
                        .crewEntity(crewEntity)
                        .memberEntity(memberEntity)
                        .message(joinDto.getMessage())
                         .build()));

        return CrewJoinRequestDto.crewJoinRequestDto(crewJoinRequestEntity);
    }

    @Override
    public List<CrewJoinRequestDto> myCrewJoinList(Long crewId) {
        // 크루아이디 확인 후 리스트 쫘라락
        return crewJoinRequestRepository.findAllByCrewEntityId(crewId).stream().map(CrewJoinRequestDto::crewJoinRequestDto).collect(Collectors.toList());

    }

    //가입 승인
    @Override
    public void crewJoinRequestApproved(CrewJoinRequestDto joinDto) {
        Long crewId = joinDto.getCrewRequestId();
        Long memberId = joinDto.getMemberRequestId();

        CrewJoinRequestEntity request = crewJoinRequestRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(()->new NullPointerException("가입신청 없음"));

                    if (request.getStatus() != RequestStatus.PENDING)
                        throw new IllegalArgumentException("이미 처리함");

//                    승인으로 변경
                    crewJoinRequestRepository.save(CrewJoinRequestEntity.updateCrewJoinApproved(request));

//                    //크루 멤버 저장
                    crewMemberRepository.save(CrewMemberEntity.insertCrewMember(request));
    }

    //가입 거절
    @Override
    public void crewJoinRequestRejected(CrewJoinRequestDto joinDto) {
        Long crewId = joinDto.getCrewRequestId();
        Long memberId = joinDto.getMemberRequestId();

        CrewJoinRequestEntity request = crewJoinRequestRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(()->new NullPointerException("가입신청 없음"));

                    if (request.getStatus() != RequestStatus.PENDING)
                        throw new IllegalArgumentException("이미 처리함");

                    //거절로 변경
                    crewJoinRequestRepository.save(CrewJoinRequestEntity.updateCrewJoinRejected(request));


    }


}
