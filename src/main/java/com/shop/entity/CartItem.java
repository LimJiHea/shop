package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity{

    @Id
    @GeneratedValue     //주키의 값을 위한 자동 생성 전략을 명시
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)       //하나의 장바구니의 여러개의 상품을 담을 수 있음 (다 대 일)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)           //하나의 상품은 여러 장바구니의 담길 수 있으므로 매핑 (다 대 일)
    @JoinColumn(name = "item_id")
    private Item item;      //장바구니에 담을 상품의 정보를 알아야 하므로 상품엔티티 매핑

    private int count;          //같은 상품을 장바구니에 몇 개 담을지 저장

    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count){            //기존에 담겨 있는 상품인데, 상품을 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 더 해줄때 사용할 메소드입니다.
        this.count += count;
    }
}
