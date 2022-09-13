package com.cNerds.dailyMoment.user.dao;

import com.cNerds.dailyMoment.core.dao.GenericDao;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.dto.UserInfoCriterion;

public interface UserInfoDao extends GenericDao<UserInfo, UserInfoCriterion> {

	UserInfo authCheckDetail(String username);

	UserInfo refreshTokenCheck(String refreshAuthToken);

	void updateAuthToken(UserInfo tokenUserInfo);
	 
}
