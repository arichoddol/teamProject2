package org.spring.backendspring.API.marathonapi.controller;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.spring.backendspring.API.marathonapi.service.MarathonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api") // React에서 fetch할 때 /api/marathons
public class MarathonController {

    private final MarathonService marathonService;

    @GetMapping("/marathons")
    public List<Marathon> marathonList() {
        // JSON 형태로 마라톤 리스트 반환
        return marathonService.findAll();
    }
}
