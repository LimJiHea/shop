package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)          //회원은 이메일은 통해 구분 -> 유니크 속성 지정
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)            //자바에 enum 타입을 엔티티 속성으로 지정 EnumType.STRING=> 순서가 바뀔 경우 문제가 되므로 string 으로 저장
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        // 멤버 엔티티를 생성하는 메소드
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());  //스프링 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean을 파라미터로 넘겨 암호화
        member.setPassword(password);
        member.setRole(Role.ADMIN);     //member 엔티티 생성시 관리자로 생성하도록 수정

        return member;
    }

}
