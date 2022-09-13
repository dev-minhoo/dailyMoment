package com.cNerds.dailyMoment.invite.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.core.dao.GenericeDaoImpl;
import com.cNerds.dailyMoment.invite.dto.InviteInfo;
import com.cNerds.dailyMoment.invite.dto.InviteInfoCriterion;
import com.cNerds.dailyMoment.photo.dto.PhotoInfo;

@Repository
public class InviteInfoDaoImpl extends GenericeDaoImpl<InviteInfo,InviteInfoCriterion> implements InviteInfoDao {

	public InviteInfoDaoImpl() {
        super(InviteInfo.class);
    }
	
	@Override
	public void inviteApprove(InviteInfo inviteInfo) {
		defaultSession.update(InviteInfo.class.getName() + ".inviteApprove",inviteInfo);
	}
	 
}
