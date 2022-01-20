package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")         //정렬시 사용하는 order 키워드가 있기 때문에 orders로 지정
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne                      //한명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑을 한다.
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;    //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    //주문상태

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
