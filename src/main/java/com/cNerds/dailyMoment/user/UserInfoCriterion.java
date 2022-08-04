package com.cNerds.dailyMoment.user;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfoCriterion extends EntityCriterionImpl {
	
	private int userNo;
	private String searchUserId;
	private String searchUserPwd;
	private String searchIsSocial;
	private String searchIsDelete;
	
}
