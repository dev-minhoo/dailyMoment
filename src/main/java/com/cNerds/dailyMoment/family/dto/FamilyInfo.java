package com.cNerds.dailyMoment.family.dto;

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
public class FamilyInfo extends EntityInfoImpl {
	
	private int familyNo;
	private String familyName;
	
	private String enFamilyNo;
	
	
	
	
	
}
