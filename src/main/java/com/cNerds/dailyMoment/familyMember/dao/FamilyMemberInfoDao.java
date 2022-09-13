package com.cNerds.dailyMoment.familyMember.dao;

import com.cNerds.dailyMoment.core.dao.GenericDao;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfoCriterion;

public interface FamilyMemberInfoDao extends GenericDao<FamilyMemberInfo, FamilyMemberInfoCriterion> {

	FamilyMemberInfo familyMemberAuthCheck(FamilyMemberInfo familyMemberInfo);

	 
}
