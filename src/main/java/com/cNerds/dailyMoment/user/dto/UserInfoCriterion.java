package com.cNerds.dailyMoment.user.dto;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfoCriterion extends EntityCriterionImpl {
	
	private String searchUserId;
	private int searchIsSocial;
	private int searchIsDelete;
	
}
