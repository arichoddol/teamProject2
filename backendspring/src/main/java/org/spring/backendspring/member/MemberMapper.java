package org.spring.backendspring.member;

import org.spring.backendspring.common.Gender;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberMapper {

    // 일반 회원가입
    public static MemberEntity toEntity(MemberDto dto,
                                        PasswordEncoder passwordEncoder) {
        return MemberEntity.builder()
                .id(dto.getId())
                .userEmail(dto.getUserEmail())
                .userPassword(passwordEncoder.encode(dto.getUserPassword()))
                .userName(dto.getUserName())
                .nickName(dto.getNickName())
                .gender(dto.getGender())
                .age(dto.getAge())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(dto.getRole())
                .isProfileImg(dto.getIsProfileImg())
                .socialLogin(0)
                .build();
    }

    // 회원 불러오기
    public static MemberDto toDto(MemberEntity entity) {
        return MemberDto.builder()
                .id(entity.getId())
                .userEmail(entity.getUserEmail())
                .userName(entity.getUserName())
                .nickName(entity.getNickName())
                .gender(entity.getGender())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .role(entity.getRole())
                .isProfileImg(entity.getIsProfileImg())
                .socialLogin(entity.getSocialLogin())
                .build();
    }

    // 소셜 로그인 처리
    public static MemberEntity toSocialEntity(String userEmail,
                                              String userName,
                                              String userPassword) {
        return MemberEntity.builder()
                .userEmail(userEmail)
                .userName(userName)
                .userPassword(userPassword)
                .isProfileImg(0)
                .nickName("닉네임을 입력해주세요.")
                .gender(Gender.MAN)
                .age(10)
                .phone("01000000000")
                .address("주소를 입력해주세요.")
                .role(MemberRole.MEMBER)
                .socialLogin(1)
                .build();
    }
}
