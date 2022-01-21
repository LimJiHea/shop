package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)       //부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeTypeAll옵션 설정            
                                                //주문 상품 엔티티와 일대다 매핑을 한다.
                                                //외래키(order_id)가 order_item 테이블에 있으므로 연관관계의 주인은 OrderItem 엔티티이다. 
                                                //Order 엔티티가 준인이 아니므로 (mappedBy)속성으로 연관관계의 주인을 설정
                                                //속성의 값으로 "order"를 적어준 이유는 OrderItem에 있는 Order에 의해 관리된다는 의미
    private List<OrderItem> orderItems = new ArrayList<>();         //하나의 주문이 여러개의 주문 상품을 갖으므로 List 자료형을 사용해서 매핑

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
