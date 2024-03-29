package com.cNerds.dailyMoment.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cNerds.dailyMoment.core.jwt.JwtAuthenticationFilter;
import com.cNerds.dailyMoment.core.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	    private final JwtTokenProvider jwtTokenProvider;

	    // authenticationManager를 Bean 등록합니다.
	    @Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {

	        http.csrf().disable();
	        //http.httpBasic().disable(); // 일반적인 루트가 아닌 다른 방식으로 요청시 거절, header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다.
	        http.httpBasic().disable()
	                .authorizeRequests()// 요청에 대한 사용권한 체크
	                .antMatchers("/user/auth/**","/resources/**","/image/**/**","/invite/**").permitAll() // 회원 로그인 및 회원가입
	                .antMatchers("/api/v1/auth/**","/",
	                        "/v2/api-docs", "/swagger-resources","/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui.html","/webjars/**",
	                        "/v3/api-docs/**","/swagger-ui/**",
	                        "/h2-console/**",
	                        "/favicon.ico").permitAll() //swagger
	                .anyRequest().authenticated()
	                .and()
	                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),UsernamePasswordAuthenticationFilter.class); 
	        		// JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
	        		// + 토큰에 저장된 유저정보를 활용하여야 하기 때문에 CustomUserDetailService 클래스를 생성합니다.
	        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


	    }
	}

