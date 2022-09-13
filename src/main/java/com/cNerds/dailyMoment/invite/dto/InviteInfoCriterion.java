package com.cNerds.dailyMoment.invite.dto;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class InviteInfoCriterion extends EntityCriterionImpl {
	private Integer searchFamilyNo;
	private String searchEmail;
	private String searchAuthCode;
	private Integer searchIsPermission;
	private Integer searchIsDelete;
	
}
