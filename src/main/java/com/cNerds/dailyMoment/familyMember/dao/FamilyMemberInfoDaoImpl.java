package com.cNerds.dailyMoment.familyMember.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.core.dao.GenericeDaoImpl;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfoCriterion;
import com.cNerds.dailyMoment.user.dto.UserInfo;

@Repository
public class FamilyMemberInfoDaoImpl extends GenericeDaoImpl<FamilyMemberInfo,FamilyMemberInfoCriterion> implements FamilyMemberInfoDao {

	public FamilyMemberInfoDaoImpl() {
        super(FamilyMemberInfo.class);
    }

	@Override
	public FamilyMemberInfo familyMemberAuthCheck(FamilyMemberInfo familyMemberInfo) {
		return defaultSession.selectOne(FamilyMemberInfo.class.getName() + ".familyMemberAuthCheck", familyMemberInfo);
	}
	 
}
