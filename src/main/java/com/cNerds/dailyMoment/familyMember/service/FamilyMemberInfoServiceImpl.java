package com.cNerds.dailyMoment.familyMember.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.family.dto.FamilyInfo;
import com.cNerds.dailyMoment.family.dto.FamilyInfoCriterion;
import com.cNerds.dailyMoment.familyMember.dao.FamilyMemberInfoDao;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfoCriterion;

@Service
public class FamilyMemberInfoServiceImpl extends GenericServiceImpl<FamilyMemberInfo, FamilyMemberInfoCriterion, FamilyMemberInfoDao> implements FamilyMemberInfoService {
	
	 
	public FamilyMemberInfoServiceImpl() {
        super(FamilyMemberInfoDao.class);
    }

	@Override
	public FamilyMemberInfo familyMemberAuthCheck(FamilyMemberInfo familyMemberInfo) {	
		return dao.familyMemberAuthCheck(familyMemberInfo);
	}
	
	@Override
	public List<FamilyMemberInfo> list(FamilyMemberInfoCriterion familyMemberCriterion) {
		List<FamilyMemberInfo> familyMemberInfoList = dao.list(familyMemberCriterion);
		if(!familyMemberInfoList.isEmpty()) {
			for(FamilyMemberInfo item : familyMemberInfoList) {
				item.setEnFamilyMemberNo(CryptoUtil.encode(Integer.toString(item.getFamilyMemberNo())));
			}
		}
		return familyMemberInfoList;
	}
	
	@Override
	public FamilyMemberInfo detail (FamilyMemberInfo familyMemberInfo) {
		familyMemberInfo = dao.detail(familyMemberInfo);
		familyMemberInfo.setEnFamilyMemberNo(CryptoUtil.encode(Integer.toString(familyMemberInfo.getFamilyNo())));
		return familyMemberInfo;
	}
		

		
	
    

}