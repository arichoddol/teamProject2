package org.spring.backendspring.config.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.config.security.util.CookieUtil;
import org.spring.backendspring.config.security.util.JWTUtil;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Log4j2
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        String email = myUserDetails.getUsername();

        MemberDto memberDto = memberService.findByUserEmail(email);
        // jwt 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(memberDto);
        String refreshToken = jwtUtil.generateRefreshToken(memberDto);

        Cookie refreshCookie = CookieUtil.createCookie("refreshToken", refreshToken);
        response.addCookie(refreshCookie);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("http://localhost:3000");

        log.info("---------------------------------------");
        log.info("OAuth2 토큰 발급 완료: {}", accessToken);
        log.info("---------------------------------------");
    }
}
