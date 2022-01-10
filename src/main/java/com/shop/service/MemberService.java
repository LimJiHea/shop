package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional      //로직을 처리하다 에러가 발생 시 변경된 데이터를 로직을 수행하기 이전 상태로 콜백
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {              //MemberService 가 UserDetailsService(데이터베이스에서 회원정보를 가져오는 역할) 을 구현한다.

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){        //이미 가입된 회원의 경우 예외 발생 IllegalStateException

        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{       //loadUserByUsername(String email)메소드 오버라이딩 email을 파라미터로 전달받음

        Member member = memberRepository.findByEmail(email);

        if (member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()           //UserDetail 을 구현하고 있는 User 객체를 반환해준다.
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


}
