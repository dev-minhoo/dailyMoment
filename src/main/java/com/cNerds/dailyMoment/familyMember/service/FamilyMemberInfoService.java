package com.cNerds.dailyMoment.familyMember.service;

import com.cNerds.dailyMoment.core.service.GenericService;
import com.cNerds.dailyMoment.familyMember.dao.FamilyMemberInfoDao;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfoCriterion;

public interface FamilyMemberInfoService extends GenericService<FamilyMemberInfo, FamilyMemberInfoCriterion, FamilyMemberInfoDao> {

	FamilyMemberInfo familyMemberAuthCheck(FamilyMemberInfo familyMemberInfo);
    

}