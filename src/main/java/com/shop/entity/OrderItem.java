package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{      //기존에 있던 regTime, updateTime 변수를 삭제후 BaseEntity를 상속

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)      //지연로딩                    //하나의 상품은 여러 주문 상품으로 들어갈 수 있으므로 주문 상품 기준으로 다대일 단방향 매핑을 설정한다.
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)                   //한번의 주문에 여러개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 주문 엔티티를 다대일 단방향 매핑을 먼저 설정한다.
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;         //주문가격

    private int count;      //주문수량

    //private LocalDateTime regTime;        삭제

   // private LocalDateTime updateTime;     삭제


    //주문할 상품, 주문 수량을 통해 OrderItem 객체를 만드는 메소드
    public static OrderItem createOderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);            //주문상품 세팅
        orderItem.setCount(count);          //주문수량 세팅
        orderItem.setOrderPrice(item.getPrice());       //현재 시간을 기준으로 상품 가격을 주문 가격으로 세팅

        item.removeStock(count);        //주문 수량만큼 상품의 재고 수량을 감소시킨다.
        return orderItem;
    }


    //주문 가격 * 주문 수량 => 해당 상품을 주문한 총 가격을 계산
    public int getTotalPrice(){
        return orderPrice * count;
    }
}
