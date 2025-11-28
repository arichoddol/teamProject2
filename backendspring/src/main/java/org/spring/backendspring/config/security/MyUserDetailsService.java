package org.spring.backendspring.config.security;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByUserEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("없는 이메일입니다."));
        if (memberEntity.isDeleted()) {
            throw new IllegalArgumentException("탈퇴한 회원입니다.");
        }
        return new MyUserDetails(memberEntity);
    }
}
