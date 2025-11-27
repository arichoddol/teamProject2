package org.spring.backendspring.admin.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.admin.service.AdminEventService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.event.dto.EventDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/event")
public class AdminEventController {

    private final AdminEventService adminEventService;

    // C - 이벤트 등록
    @PostMapping
    public EventDto create(@RequestBody EventDto dto) {
        return adminEventService.createEvent(dto);
    }

    // R - 이벤트 목록(검색 + 페이징)
    @GetMapping
    public PagedResponse<EventDto> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminEventService.getEventList(keyword, page, size);
    }

    // R - 이벤트 상세
    @GetMapping("/{id}")
    public EventDto detail(@PathVariable Long id) {
        return adminEventService.getEventDetail(id);
    }

    // U - 이벤트 수정
    @PutMapping("/{id}")
    public EventDto update(@PathVariable Long id, @RequestBody EventDto dto) {
        return adminEventService.updateEvent(id, dto);
    }

    // D - 이벤트 삭제
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @RequestParam String role) {
        adminEventService.deleteEvent(id, role);
        return "삭제되었습니다.";
    }
}
