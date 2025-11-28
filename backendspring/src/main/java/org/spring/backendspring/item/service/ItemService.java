package org.spring.backendspring.item.service;

import java.util.List;

import org.spring.backendspring.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    // Read Only 
    Page<ItemDto> pagingSearchItemList(Pageable pageable, String subject, String search);

    ItemDto itemDetail(Long itemId);

    // category Paging
    Page<ItemDto> getItemsByCategory(Pageable pageable, String category, String subject, String search);

    // C U D -> Admin function 
    // CheckOut Admin Dir...
    List<ItemDto> getRecentItem();
}
