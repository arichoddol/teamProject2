package org.spring.backendspring;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void insert() {
        Optional<MemberEntity> byId = memberRepository.findById(1L);
        for (int i = 0; i < 10; i++) {
            itemRepository.save(
                    ItemEntity.builder()
                            .itemTitle("ㅎㅇ" + i)
                            .itemDetail("ㅎㅇㅎㅇ")
                            .itemPrice(1000)
                            .attachFile(0)
                            .itemSize(1)
                            .memberEntity(MemberEntity.builder()
                                    .id(byId.get().getId())
                                    .build())
                            .build()
            );
        }
    }
}
