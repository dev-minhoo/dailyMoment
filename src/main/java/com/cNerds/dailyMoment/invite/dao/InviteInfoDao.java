package com.cNerds.dailyMoment.invite.dao;

import com.cNerds.dailyMoment.core.dao.GenericDao;
import com.cNerds.dailyMoment.invite.dto.InviteInfo;
import com.cNerds.dailyMoment.invite.dto.InviteInfoCriterion;

public interface InviteInfoDao extends GenericDao<InviteInfo, InviteInfoCriterion> {

	void inviteApprove(InviteInfo inviteInfo);

	 
}
