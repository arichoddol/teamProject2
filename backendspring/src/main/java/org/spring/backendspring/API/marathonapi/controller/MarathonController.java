package org.spring.backendspring.API.marathonapi.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.spring.backendspring.API.marathonapi.service.MarathonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MarathonController {

    private final MarathonService marathonService;

    @GetMapping({ "/marathons", "/marathon" })
    public Page<Marathon> marathonList(
            @RequestParam(required = false) String searchTerm, // 검색어 (선택 사항)
            @PageableDefault(size = 10) Pageable pageable // 페이징 정보 (기본 10개)
    ) {
        // 검색어가 있으면 검색 로직을, 없으면 전체 리스트 페이징을 호출
        return marathonService.findMarathons(searchTerm, pageable);
    }
}