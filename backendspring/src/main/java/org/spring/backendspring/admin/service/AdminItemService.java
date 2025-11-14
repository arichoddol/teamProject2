package org.spring.backendspring.admin.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.item.dto.ItemDto;

public interface AdminItemService {

    ItemDto findById(Long id);

    void insertItem(ItemDto itemDto);

    ItemDto updateItem(Long id, ItemDto updatedDto);

    void deleteItem(Long id);

    PagedResponse<ItemDto> findAllItems(String keyword, int page, int size);

}
