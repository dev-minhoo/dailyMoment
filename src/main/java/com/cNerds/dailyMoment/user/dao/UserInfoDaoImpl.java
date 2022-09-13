package com.cNerds.dailyMoment.user.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.core.dao.GenericeDaoImpl;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.dto.UserInfoCriterion;

@Repository
public class UserInfoDaoImpl extends GenericeDaoImpl<UserInfo,UserInfoCriterion> implements UserInfoDao {

	public UserInfoDaoImpl() {
        super(UserInfo.class);
    }

	@Override
	public UserInfo authCheckDetail(String userId) {
		
		return defaultSession.selectOne(UserInfo.class.getName() + ".authCheckDetail", userId);
	}

	@Override
	public UserInfo refreshTokenCheck(String refreshAuthToken) {
		return defaultSession.selectOne(UserInfo.class.getName() + ".refreshTokenCheck", refreshAuthToken);
	}

	@Override
	public void updateAuthToken(UserInfo tokenUserInfo) {
		defaultSession.update(UserInfo.class.getName() + ".updateAuthToken", tokenUserInfo);
		
	}

	

	 
}
