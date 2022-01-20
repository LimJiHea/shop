package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne           //회원 엔티티와 1대1로 매핑 => 따로 옵션을 주지않으면 (FetchType.EAGER) 즉시로딩 설정과 동일
    @JoinColumn(name = "member_id") //JoinColumn 어노테이션으로 매핑할 외래키 지정 (name = "외래키의 이름을 설정")
    private Member member;

}
