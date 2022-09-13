package com.cNerds.dailyMoment.invite.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.invite.dao.InviteInfoDao;
import com.cNerds.dailyMoment.invite.dto.InviteInfo;
import com.cNerds.dailyMoment.invite.dto.InviteInfoCriterion;
import com.cNerds.dailyMoment.like.dto.LikeInfo;

@Service
public class InviteInfoServiceImpl extends GenericServiceImpl<InviteInfo, InviteInfoCriterion, InviteInfoDao> implements InviteInfoService {
	
	 
	public InviteInfoServiceImpl() {
        super(InviteInfoDao.class);
    }
	
	@Override
	public List<InviteInfo> list(InviteInfoCriterion inviteInfoCriterion) {
		List<InviteInfo> inviteInfoList = dao.list(inviteInfoCriterion);
		if(!inviteInfoList.isEmpty()) {
			for(InviteInfo item : inviteInfoList) {
				item.setEnInviteNo(CryptoUtil.encode(Integer.toString(item.getInviteNo())));
			}
		}
		return inviteInfoList;
	}
	
	@Override
	public InviteInfo detail (InviteInfo inviteInfo) {
		inviteInfo = dao.detail(inviteInfo);
		inviteInfo.setEnInviteNo(CryptoUtil.encode(Integer.toString(inviteInfo.getInviteNo())));
		return inviteInfo;
	}
	
	@Override
	public void inviteApprove(InviteInfo inviteInfo) {
		dao.inviteApprove(inviteInfo);
	}

	
    

}