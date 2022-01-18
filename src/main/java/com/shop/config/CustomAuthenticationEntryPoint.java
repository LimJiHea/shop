package com.shop.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* /auth 라는 Path는 스프링 시큐리티 컨텍스트 내에 존재하는 인증절차를 거쳐 통과
    =>인증과정에서 실패하거나 인증헤더(Authorization)를 보내지 않게되는 경우 401(UnAuthorized) 라는 응답값
    =>이를 처리해주는 로직이 바로 AuthenticationEntryPoint라는 인터페이스
    =>Response에 401이 떨어질만한 에러가 발생할 경우 해당로직을 타게되어, commence라는 메소드를 실행
     */


public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override public void commence(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException authException) throws IOException, ServletException{
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        // 인증되지 않은 사용자가 리소스를 요청할 경우 "Unauthorized" 에러를 발생하도록 인터페이스 구현
    }
}
