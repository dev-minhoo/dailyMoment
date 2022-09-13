package com.cNerds.dailyMoment.like.dto;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class LikeInfoCriterion extends EntityCriterionImpl {
	private int searchPhotoNo;
	private int searchReplyNo;
	private int searchUserNo;
	
}
