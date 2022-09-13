package com.cNerds.dailyMoment.family.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.children.dto.ChildrenInfo;
import com.cNerds.dailyMoment.children.dto.ChildrenInfoCriterion;
import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.family.dao.FamilyInfoDao;
import com.cNerds.dailyMoment.family.dto.FamilyInfo;
import com.cNerds.dailyMoment.family.dto.FamilyInfoCriterion;

@Service
public class FamilyInfoServiceImpl extends GenericServiceImpl<FamilyInfo, FamilyInfoCriterion, FamilyInfoDao> implements FamilyInfoService {
	
	 
	public FamilyInfoServiceImpl() {
        super(FamilyInfoDao.class);
    }
		

	@Override
	public List<FamilyInfo> list(FamilyInfoCriterion familyCriterion) {
		List<FamilyInfo> familyInfoList = dao.list(familyCriterion);
		if(!familyInfoList.isEmpty()) {
			for(FamilyInfo item : familyInfoList) {
				item.setEnFamilyNo(CryptoUtil.encode(Integer.toString(item.getFamilyNo())));
			}
		}
		return familyInfoList;
	}
	
	@Override
	public FamilyInfo detail (FamilyInfo familyInfo) {
		familyInfo = dao.detail(familyInfo);
		familyInfo.setEnFamilyNo(CryptoUtil.encode(Integer.toString(familyInfo.getFamilyNo())));
		return familyInfo;
	}
	
    

}