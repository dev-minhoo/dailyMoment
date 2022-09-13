package com.cNerds.dailyMoment.user.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.user.dao.UserInfoDao;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.dto.UserInfoCriterion;

@Service
public class UserInfoServiceImpl extends GenericServiceImpl<UserInfo, UserInfoCriterion, UserInfoDao> implements UserInfoService,UserDetailsService {
	
	 
	public UserInfoServiceImpl() {
        super(UserInfoDao.class);
    }
		// JWT 유저 인증
		@Override
		public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {			
			UserInfo loginUser = dao.authCheckDetail(userId);
		return loginUser;
	}
		@Override
		public UserInfo userCheck(String userId) {
			UserInfo loginUser = dao.authCheckDetail(userId);
			if(loginUser != null) {
				loginUser.setEnUserNo(CryptoUtil.encode(Integer.toString(loginUser.getUserNo())));
			}
			return loginUser;
		}
		
		@Override
		public List<UserInfo> list(UserInfoCriterion userInfoCriterion) {
			List<UserInfo> userInfoList = dao.list(userInfoCriterion);
			if(!userInfoList.isEmpty()) {
				for(UserInfo item : userInfoList) {
					item.setEnUserNo(CryptoUtil.encode(Integer.toString(item.getUserNo())));
				}
			}
			return userInfoList;
		}
		
		@Override
		public UserInfo detail (UserInfo userInfo) {
			userInfo = dao.detail(userInfo);
			userInfo.setEnUserNo(CryptoUtil.encode(Integer.toString(userInfo.getUserNo())));
			return userInfo;
		}
		@Override
		public UserInfo refreshTokenCheck(String refreshAuthToken) {			
			return dao.refreshTokenCheck(refreshAuthToken);
		}
		@Override
		public void updateAuthToken(UserInfo tokenUserInfo, UserInfo modifier) {
			tokenUserInfo.setModifier(modifier);
			dao.updateAuthToken(tokenUserInfo);
		}

		
	
    

}