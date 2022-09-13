package com.cNerds.dailyMoment.like.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfoCriterion;
import com.cNerds.dailyMoment.like.dao.LikeInfoDao;
import com.cNerds.dailyMoment.like.dto.LikeInfo;
import com.cNerds.dailyMoment.like.dto.LikeInfoCriterion;

@Service
public class LikeInfoServiceImpl extends GenericServiceImpl<LikeInfo, LikeInfoCriterion, LikeInfoDao> implements LikeInfoService {
	
	 
	public LikeInfoServiceImpl() {
        super(LikeInfoDao.class);
    }
	
	@Override
	public List<LikeInfo> list(LikeInfoCriterion likeInfoCriterion) {
		List<LikeInfo> likeInfoList = dao.list(likeInfoCriterion);
		if(!likeInfoList.isEmpty()) {
			for(LikeInfo item : likeInfoList) {
				item.setEnLikeNo(CryptoUtil.encode(Integer.toString(item.getLikeNo())));
			}
		}
		return likeInfoList;
	}
	
	@Override
	public LikeInfo detail (LikeInfo likeInfo) {
		likeInfo = dao.detail(likeInfo);
		likeInfo.setEnLikeNo(CryptoUtil.encode(Integer.toString(likeInfo.getLikeNo())));
		return likeInfo;
	}
    

}