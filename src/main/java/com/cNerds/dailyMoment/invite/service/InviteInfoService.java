package com.cNerds.dailyMoment.invite.service;

import com.cNerds.dailyMoment.core.service.GenericService;
import com.cNerds.dailyMoment.invite.dao.InviteInfoDao;
import com.cNerds.dailyMoment.invite.dto.InviteInfo;
import com.cNerds.dailyMoment.invite.dto.InviteInfoCriterion;

public interface InviteInfoService extends GenericService<InviteInfo, InviteInfoCriterion, InviteInfoDao> {
	
	void inviteApprove(InviteInfo inviteInfo);

}