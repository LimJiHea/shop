package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue     //주키의 값을 위한 자동 생성 전략을 명시
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne      //하나의 장바구니의 여러개의 상품을 담을 수 있음 (다 대 일)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne          //하나의 상품은 여러 장바구니의 담길 수 있으므로 매핑 (다 대 일)
    @JoinColumn(name = "item_id")
    private Item item;      //장바구니에 담을 상품의 정보를 알아야 하므로 상품엔티티 매핑

    private int count;          //같은 상품을 장바구니에 몇 개 담을지 저장
}