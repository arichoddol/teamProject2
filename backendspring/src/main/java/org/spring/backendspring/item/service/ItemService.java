package org.spring.backendspring.item.service;

import org.spring.backendspring.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    // Read Only 
    Page<ItemDto> pagingSearchItemList(Pageable pageable, String subject, String search);

    ItemDto itemDetail(Long itemId);


    // C U D -> Admin function 
    // CheckOut Admin Dir...
    
}
