package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
// 주문 로직
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId())       //주문할 상품 조회
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);        //현재 로그인한 회원이메일 정보를 이용해 회원정보 조회 

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOderItem(item, orderDto.getCount());      //주문할 상품 엔티티와 주문 수량을 이용해 주문 상품 엔티티 생성
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);     //회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티 생성
        orderRepository.save(order);        //생성한 주문 엔티티 저장

        return order.getId();
    }


    //주문 목록을 조회
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){

        List<Order> orders = orderRepository.findOrders(email,pageable);   //유저 아이디와 페이징 조건을 이용하여 주문 목록을 조회한다.
        Long totalCount = orderRepository.countOrder(email);    //유저의 주문 총 개수를 구한다.

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders){                                 //주문 리스트를 순회하며 구매 이력 페이지에 전달할 DTO를 생성한다.
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems){
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");  //주문상품 대표이미지 조회
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);     //페이지 구현 객체를 생성하여 반환한다.
    }



}
