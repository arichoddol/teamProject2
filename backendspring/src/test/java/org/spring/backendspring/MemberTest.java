package org.spring.backendspring;

import org.junit.jupiter.api.Test;
import org.spring.backendspring.common.Gender;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.member.entity.MemberEntity;

import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void createMembers() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        int memberCount = 10; // 생성할 회원 수

        for (int i = 1; i <= memberCount; i++) {
            String email = "user" + i + "@test.com";
            String rawPassword = "pw" + i;
            String encodedPassword = encoder.encode(rawPassword);
            String nickName = "nick" + i;
            String userName = "User" + i;

            // Gender enum 사용
            Gender gender = (i % 2 == 0) ? Gender.MAN : Gender.WOMAN;

            // Role enum 사용
            MemberRole role = MemberRole.MEMBER;

            MemberEntity member = MemberEntity.builder()
                    .userEmail(email)
                    .userPassword(encodedPassword)
                    .nickName(nickName)
                    .userName(userName)
                    .gender(gender)
                    .age(20 + i)
                    .phone("010-1111-11" + String.format("%02d", i))
                    .address("서울시 강남구")
                    .role(role)
                    .isProfileImg(0)
                    .socialLogin(0)
                    .build();

            memberRepository.save(member);
        }

        System.out.println(memberCount + "명의 회원 생성 완료!");
    }
}
