package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//주문 정보를 담을 Dto
@Getter @Setter
public class OrderHistDto {

    public OrderHistDto(Order order){
        this.orderId = order.getId();
        //화면에 주문날짜를 yyyy-mm-dd HH:mm 형태로 전달하기위해 format 수정
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId;       //주문 아이디

    private String orderDate;       //주문 날짜

    private OrderStatus orderStatus;       //주문 상태

    //주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    //orderItemDto 객체를 주문 상품 리스트에 추가하는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
