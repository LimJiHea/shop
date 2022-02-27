package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    //상품 조회 조건 : itemSearchDto , 페이징 정보 Pageable 객체를 파라미터로 받는다.
    // 반환 데이터 : Page<Item>
}
