package com.cNerds.dailyMoment.family.dto;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class FamilyInfoCriterion extends EntityCriterionImpl {
	
	private String searchFamilyName;
	private FamilyMemberInfo familyMemberInfo;
	
}
