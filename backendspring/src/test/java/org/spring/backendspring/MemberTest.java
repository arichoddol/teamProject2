package org.spring.backendspring;

import org.junit.jupiter.api.Test;
import org.spring.backendspring.common.Gender;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void insert() {
        for (int i = 0; i < 5; i++) {
            memberRepository.save(
                MemberEntity.builder()
                        .userEmail("a" + i  + "@email.com")
                        .userPassword(passwordEncoder.encode("11"))
                        .nickName("a" + i)
                        .userName("a" + i)
                        .gender(Gender.MAN)
                        .age(11)
                        .phone("01000000000")
                        .address("서울")
                        .role(MemberRole.MEMBER)
                        .isProfileImg(0)
                        .socialLogin(0)
                        .build()
            );
        }
    }

}
