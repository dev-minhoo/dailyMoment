package com.cNerds.dailyMoment.user.service;

import com.cNerds.dailyMoment.core.service.GenericService;
import com.cNerds.dailyMoment.user.dao.UserInfoDao;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.dto.UserInfoCriterion;

public interface UserInfoService extends GenericService<UserInfo, UserInfoCriterion, UserInfoDao> {

	UserInfo userCheck(String userId);

	UserInfo refreshTokenCheck(String refreshAuthToken);

	void updateAuthToken(UserInfo tokenUserInfo, UserInfo tokenUserInfo2);
    

}