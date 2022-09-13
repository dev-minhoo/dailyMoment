package com.cNerds.dailyMoment.like.dto;

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
public class LikeInfo extends EntityInfoImpl {
	
	private int likeNo;
	private int photoNo;
	private int replyNo;
	
	private String enLikeNo;
	
	
	
	
	
}
