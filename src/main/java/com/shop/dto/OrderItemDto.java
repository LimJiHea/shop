package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

//주문상품 정보를 화면에 보낼때 사용할 Dto
@Getter @Setter
public class OrderItemDto {

    //OrderItemDto 클래스의 생성자로 orderItem객체와 이미지 경로를 파라미터로 받아서 멤버 변수 값을 세팅한다.
    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm;         //상품명

    private int count;      //주문 수량

    private int orderPrice;     //주문 금액

    private String imgUrl;      //상품 이미지 경로


}
