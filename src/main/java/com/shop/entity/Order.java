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
public class Order extends BaseEntity{      //regTime, updateTime 삭제 후 BaseEntity 상속

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)         //한명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑을 한다.
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;    //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)       //부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeTypeAll옵션 설정
                                                                                        //orphanRemoval = true  고아객체 제거
                                                //주문 상품 엔티티와 일대다 매핑을 한다.
                                                //외래키(order_id)가 order_item 테이블에 있으므로 연관관계의 주인은 OrderItem 엔티티이다. 
                                                //Order 엔티티가 준인이 아니므로 (mappedBy)속성으로 연관관계의 주인을 설정
                                                //속성의 값으로 "order"를 적어준 이유는 OrderItem에 있는 Order에 의해 관리된다는 의미
    private List<OrderItem> orderItems = new ArrayList<>();         //하나의 주문이 여러개의 주문 상품을 갖으므로 List 자료형을 사용해서 매핑

    //private LocalDateTime regTime;        삭제

    //private LocalDateTime updateTime;     삭제


    //주문 상품 객체를 이용하여 주문객체 만들기
    public void addOrderItem(OrderItem orderItem){
        // orderItem : 주문 상품 정보들을 담아준다.
        orderItems.add(orderItem);
        orderItem.setOrder(this);       //Order : OrderItem  양방향 참조관계이므로 OrderItem 객체에도 order객체 세팅
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);            //상품을 주문한 회원정보 세팅

        for(OrderItem orderItem : orderItemList){       //상품 페이지에서는 1개의 상품을 주문하지만 장바구니 페이지에서 한번에 여러개의 상품을 주문할 수 있으므로
                                                        // 여러개의 주문상품을 받을 수 있도록 리스트형태로 파라미터 값을 받으며 주문 객체에 orderItem 객체를 추가한다.
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);        //주문상태 :ORDER 세팅
        order.setOrderDate(LocalDateTime.now());        //현재 시간을 주문 시간으로 세팅한다.
        return order;
    }

    //총 주문 금액 구하는 메소드
    public int getTotalPrice(){
        int totalPrice =0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
