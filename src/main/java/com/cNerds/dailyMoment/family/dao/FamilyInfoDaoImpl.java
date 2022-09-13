package com.cNerds.dailyMoment.family.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.core.dao.GenericeDaoImpl;
import com.cNerds.dailyMoment.family.dto.FamilyInfo;
import com.cNerds.dailyMoment.family.dto.FamilyInfoCriterion;
import com.cNerds.dailyMoment.user.dto.UserInfo;

@Repository
public class FamilyInfoDaoImpl extends GenericeDaoImpl<FamilyInfo,FamilyInfoCriterion> implements FamilyInfoDao {

	public FamilyInfoDaoImpl() {
        super(FamilyInfo.class);
    }
	 
}
