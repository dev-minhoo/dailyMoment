package com.cNerds.dailyMoment.familyMember.dto;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class FamilyMemberInfoCriterion extends EntityCriterionImpl {
	
	private Integer searchFamilyNo;
	private Integer searchUserNo;
	private String searchUserId;
	
	private Integer searchIsFamilyAdmin;
	private Integer searchIsFamilyAgree;
	
}
