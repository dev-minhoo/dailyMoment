package com.cNerds.dailyMoment.user.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cNerds.dailyMoment.core.entity.EntityInfoImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo extends EntityInfoImpl implements UserDetails {
	
	private int userNo;
	private String userId;
	private String userPwd;
	private String userName;
	private String userNickName;
	private int isAgree;
	private int isSocial;		// 0 : 자체 1: 카카오 2: 네이버 3 : 구글 
	private String userAuthCheckToken;
	private String imageName;
	
	private int isInvate;
	
	private String enUserNo;

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority(this.getUserId()));
        return authList;
    }
	@Override
	public String getPassword() {
		
		return userPwd;
	}

	@Override
	public String getUsername() {
		
		return userName;
	}
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
	
	
	
	
}
