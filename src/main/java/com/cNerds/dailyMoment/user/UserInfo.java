package com.cNerds.dailyMoment.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.mapping.FetchType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;

public class UserInfo implements UserDetails {

	
	private int userNo;
	private String userId;
	private String userPwd;
	private String userName;
	private String userNickName;
	private String isAgree;
	private String isSocial;
	private String isDelete;
	private String role;

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority(this.getUsername()));
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
