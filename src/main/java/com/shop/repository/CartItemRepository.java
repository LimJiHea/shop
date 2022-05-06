package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);       //카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회한다.

    
    //CartDetailDto의 생성자를 이용하여 DTO를 반환할 때 "new 패키지+클래스명(파라미터순서 = DTO 클래스의 명시한 순서)
    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +                        //장바구니에 담겨있는 상품에 대표이미지만 가져오도록 조건문 세팅
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
