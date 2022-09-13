package com.cNerds.dailyMoment.invite.dto;

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
public class InviteInfo extends EntityInfoImpl {
	
	private int inviteNo;
	private int familyNo;
	private String familyRelation;
	private String email;
	private String authCode;
	private int isPermission; // 회원초대 확인 후 가입시
	private int isDelete; // 회원초대 삭제시, 회원초대는 메일을 보내기 때문에 이력을 남기는게 좋다고 판단

	private String enInviteNo;
	
	
	
	
	
}
