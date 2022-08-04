package com.cNerds.dailyMoment.user.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.core.dao.myBatis.MyBatisGenericDao;
import com.cNerds.dailyMoment.user.UserInfo;
import com.cNerds.dailyMoment.user.UserInfoCriterion;

@Repository
public class UserInfoDao extends MyBatisGenericDao<UserInfo, UserInfoCriterion> {
	
	public UserInfoDao() {
		super(UserInfo.class);
	}
	
}