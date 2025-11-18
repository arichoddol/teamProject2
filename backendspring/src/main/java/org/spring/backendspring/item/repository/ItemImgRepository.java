package org.spring.backendspring.item.repository;

import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImgEntity, Long> {

    ItemImgEntity findByItemEntity(ItemEntity existingItem);
    
}
