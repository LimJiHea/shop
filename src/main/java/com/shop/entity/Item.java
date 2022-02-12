package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")     //어떤 테이블과 매핑될지 정함
@Getter
@Setter
@ToString
public class Item extends BaseEntity{           //regTime, updateTime 삭제 후 BaseEntity 상속


    @Id                     //기본키
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO) //기본키 생성전략 AUTO
    private Long id;    //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm;  //상품명

    @Column(name = "price", nullable = false)
    private int price;  //가격

    @Column(nullable = false)
    private int stockNumber;    //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품 판매 상태

   // private LocalDateTime regTime;  //등록시간  (삭제)

   // private LocalDateTime updateTime;   //수정 시간      (삭제)


    //상품데이터를 업데이트하는 로직
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
}
