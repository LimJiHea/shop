package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

//주문 관련 요청 처리 컨트롤러
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    //비동기 처리 ResponseBody(자바 객체 -> HTTP 요청의 body전달) RequestBody(HTTP요청의 Body를 -> 자바 객체로 전달)
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal){

        if (bindingResult.hasErrors()){     //주문정보를 받는 orderDto객체에 데이터 바인딩 시 에러가 있는지 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);   //에러 정보를 ResponseEntity객체에 담아 반환
        }

        String email = principal.getName();     //principal => @controller 어노테이션이 선언된 클래스에서 메소드 인자로 principal객체를 
                                                //넘겨줄 경우 해당 객체에 직접 접근 가능 (현재 로그인한 회원의 이메일 정보를 조회한다.)
        Long orderId;

        try{
            orderId = orderService.order(orderDto, email);      //화면으로부터 넘어오는 주문 정보와 회원의 이메일 정보를 이용하여 주문 로직을 호출한다.
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);      //결과값으로 생성된 주문 번호와 요청이 성공했다는 HTTP 응답 상태 코드를 반환한다.
    }

    //구매이력 조회
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,4);        //한번에 가져올 주문 개수 4개

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return "order/orderHist";
    }

    //주문 번호(orderId)를 받아서 주문 취소  => 비동기 
    @PostMapping(value = "/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){

        if (!orderService.validateOrder(orderId, principal.getName())){         //주문취소 권한 검사 => 자바스크립트에서 주문번호 조작이 가능하기 때문
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
