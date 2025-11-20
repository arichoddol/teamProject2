package org.spring.backendspring.admin.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.item.dto.ItemDto;
import org.springframework.web.multipart.MultipartFile;

public interface AdminItemService {

    ItemDto findById(Long id);

    void insertItem(ItemDto itemDto, MultipartFile itemFile, Long memberId);

    ItemDto updateItem(Long id, ItemDto updatedDto, MultipartFile itemFile, Long memberId);

    void deleteItem(Long id);

    PagedResponse<ItemDto> findAllItems(String keyword, int page, int size);

    public void deleteImage(Long id);
}
