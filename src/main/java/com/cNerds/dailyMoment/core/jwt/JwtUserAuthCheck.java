package com.cNerds.dailyMoment.core.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;
import com.cNerds.dailyMoment.familyMember.service.FamilyMemberInfoService;
import com.cNerds.dailyMoment.like.dto.LikeInfo;
import com.cNerds.dailyMoment.like.service.LikeInfoService;
import com.cNerds.dailyMoment.reply.dto.ReplyInfo;
import com.cNerds.dailyMoment.reply.service.ReplyInfoService;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.service.UserInfoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtUserAuthCheck {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private FamilyMemberInfoService familyMemberInfoService;
	
	@Autowired	
	private ReplyInfoService replyInfoService;
	
	@Autowired
	private LikeInfoService likeInfoService;
	
	// 각각 권한 체크
	
	// 1. user 권한
	public Boolean userAuthCheck(String accessAuthToken, int userNo) {
		Boolean authCheck = false;		
		String authCheckId = jwtTokenProvider.getUserPk(accessAuthToken);
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUserNo(userNo);
		userInfo = userInfoService.detail(userInfo);
		
		if(userInfo.getUserId().equals(authCheckId)) {
			authCheck = true;
		}
		return authCheck;
	}
	
	// 2. familyMemberInfo 관리자 권한
	public Boolean familyAdminAuthCheck(UserInfo userInfo, int familyNo) {
		Boolean authCheck = false;		
		FamilyMemberInfo familyMemberInfo = new FamilyMemberInfo();
		familyMemberInfo.setUserNo(userInfo.getUserNo());
		familyMemberInfo.setFamilyNo(familyNo);
		familyMemberInfo.setIsFamilyAdmin(1);
		familyMemberInfo = familyMemberInfoService.familyMemberAuthCheck(familyMemberInfo);
		if(familyMemberInfo != null ) {
			authCheck = true;
		}
		return authCheck;
	}
	
	// 3. familyMemberInfo 권한
	public Boolean familyMemberAuthCheck(UserInfo userInfo, int familyNo) {
		Boolean authCheck = false;		
		FamilyMemberInfo familyMemberInfo = new FamilyMemberInfo();
		familyMemberInfo.setUserNo(userInfo.getUserNo());
		familyMemberInfo.setFamilyNo(familyNo);
		
		familyMemberInfo = familyMemberInfoService.familyMemberAuthCheck(familyMemberInfo);
		if(familyMemberInfo != null ) {
			authCheck = true;
		}
		return authCheck;
	}
	
	// 4. replyInfo 권한
		public Boolean replyAuthCheck(UserInfo userInfo, int replyNo) {
			Boolean authCheck = false;
			
			ReplyInfo replyInfo = new ReplyInfo();
			replyInfo.setReplyNo(replyNo);
			replyInfo = replyInfoService.detail(replyInfo);
			
			if(userInfo.getUserNo() ==  replyInfo.getCreator().getUserNo()) {
				authCheck = true;
			}
			return authCheck;
		}
		
		// 5. likeInfo 권한
			public Boolean likeAuthCheck(UserInfo userInfo, int likeNo) {
				Boolean authCheck = false;
				
				LikeInfo likeInfo = new LikeInfo();
				likeInfo.setLikeNo(likeNo);;
				likeInfo = likeInfoService.detail(likeInfo);
				
				if(userInfo.getUserNo() ==  likeInfo.getCreator().getUserNo()) {
					authCheck = true;
				}
				return authCheck;
			}
	
	
	
	
	
}
