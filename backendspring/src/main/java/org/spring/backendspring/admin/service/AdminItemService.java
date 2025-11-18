package org.spring.backendspring.admin.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.item.dto.ItemDto;
import org.springframework.web.multipart.MultipartFile;

public interface AdminItemService {

    ItemDto findById(Long id);

    void insertItem(ItemDto itemDto, MultipartFile itemFile);

    ItemDto updateItem(Long id, ItemDto updatedDto, MultipartFile itemFile);

    void deleteItem(Long id);

    PagedResponse<ItemDto> findAllItems(String keyword, int page, int size);

}
