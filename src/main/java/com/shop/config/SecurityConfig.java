package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/members/login")    //로그인 페이지 URL 설정
                .defaultSuccessUrl("/")         //로그인 성공시 이동할 URL 설정
                .usernameParameter("email")     //로그인 시 사용할 파라미터 이름으로 email을 지정
                .failureUrl("/members/login/error") //로그인 실패시 이동할 URL 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))     //로그아웃 URL 설정
                .logoutSuccessUrl("/")      //로그아웃 성공시 이동할 URL설정
                ;

        http.authorizeRequests()
                .mvcMatchers("/","/members/**","/item/**","/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                ;

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**","/js/**","/img/**");
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{       //Builder가 매니저를 생성해서 인증한다
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());        //비밀번호 암호화를 위해 지정해준다.
    }
}
