package com.cNerds.dailyMoment.reply.dto;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class ReplyInfoCriterion extends EntityCriterionImpl {
		private int searchPhotoNo;
		private int searchParentReplyNo;
	
	
}
