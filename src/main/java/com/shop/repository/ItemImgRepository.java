package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);            //매개변수로 넘겨준 상품 아이디를 가지며, 상품 이미지 아이디의 오름차순으로 가져오는 쿼리 메소드


    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);      //상품 대표 이미지 찾는 쿼리 메소드 추가 (구매 이력페이지에서 주문상품 대표이미지를 보여준다.)
}
