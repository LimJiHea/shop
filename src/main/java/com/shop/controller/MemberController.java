package com.shop.controller;


import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){      //@Valid => 검증

        if (bindingResult.hasErrors()){     //검사 후 결과는 bindingResult 에 담아준다.
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){                                       //e.getMessage() : 에러 이벤트와 함께 들어오는 메세지 출력
                                                                                // e.toString() : 에러 이벤트의 toString()을 호출해서 간단한 에러 메시지 확인
                                                                                //e.printStackTrace() : 에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
            model.addAttribute("errorMessage",e.getMessage());      //회원가입 시 중복 회원 가입 예외가 발생하면 에러 메시지를 뷰로 전달한다.
            return "member/memberForm";
        }

        return "redirect:/";

    }


}
