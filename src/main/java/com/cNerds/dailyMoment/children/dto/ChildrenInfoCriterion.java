package com.cNerds.dailyMoment.children.dto;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class ChildrenInfoCriterion extends EntityCriterionImpl {
	
	private int searchfamilyNo;
	
}
