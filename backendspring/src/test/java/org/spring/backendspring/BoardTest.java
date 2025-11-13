package org.spring.backendspring;

import org.junit.jupiter.api.Test;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.repository.BoardRepository;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void insertDummyBoards() {

        MemberEntity writer = memberRepository.findById(1L).orElse(null);

        for (int i = 1; i <= 30; i++) {

            boardRepository.save(
                    BoardEntity.builder()
                            .title("테스트 게시글 제목 " + i)
                            .content("테스트 게시글 내용 " + i)
                            .attachFile(0)
                            .hit(0)
                            .memberEntity(writer) // null이어도 그냥 감
                            .build());
        }

        System.out.println("게시글 30개 생성 완료");
    }
}
