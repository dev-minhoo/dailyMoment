package com.cNerds.dailyMoment.core.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.cNerds.dailyMoment.user.dto.UserInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String accessAuthToken = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        String refreshAuthToken = jwtTokenProvider.resolveRefreshToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인합니다.
        if (accessAuthToken != null && jwtTokenProvider.validateToken(accessAuthToken)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(accessAuthToken);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            ((HttpServletResponse) response).setHeader("accessAuthToken", accessAuthToken);
            ((HttpServletResponse) response).setHeader("refreshAuthToken", refreshAuthToken);
            chain.doFilter(request, response);
        }else {
        	if(refreshAuthToken != null && jwtTokenProvider.validateToken(refreshAuthToken)) {
        		
        		UserInfo tokenUserInfo = jwtTokenProvider.refreshTokenUserCheck(refreshAuthToken);
        		if(tokenUserInfo != null) {
        			JwtAuthToken token = jwtTokenProvider.reCreateToken(tokenUserInfo);
            		// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            		Authentication authentication = jwtTokenProvider.getAuthentication(token.getAccessAuthToken());
            		SecurityContextHolder.getContext().setAuthentication(authentication);
	
                    ((HttpServletResponse) response).setHeader("accessAuthToken", token.getAccessAuthToken());
                    ((HttpServletResponse) response).setHeader("refreshAuthToken", token.getRefreshAuthToken());
        		}
                chain.doFilter(request, response);
                
        	}else {	
        		// 403
        		//((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
        		chain.doFilter(request, response);
        	}
        }    
    }
    
    
    
}
