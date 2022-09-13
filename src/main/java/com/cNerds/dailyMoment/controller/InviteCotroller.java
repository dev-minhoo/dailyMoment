package com.cNerds.dailyMoment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cNerds.dailyMoment.core.jwt.JwtTokenProvider;
import com.cNerds.dailyMoment.core.jwt.JwtUserAuthCheck;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.core.util.MailUtil;
import com.cNerds.dailyMoment.family.dto.FamilyInfo;
import com.cNerds.dailyMoment.family.service.FamilyInfoService;
import com.cNerds.dailyMoment.invite.dto.InviteInfo;
import com.cNerds.dailyMoment.invite.dto.InviteInfoCriterion;
import com.cNerds.dailyMoment.invite.service.InviteInfoService;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.service.UserInfoService;

@RestController
@RequestMapping("/invite/")
public class InviteCotroller {
	
	@Autowired
	private JwtUserAuthCheck jwtUserAuthCheck;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private FamilyInfoService familyInfoService;

	@Autowired
	private InviteInfoService inviteInfoService;
	
	@Autowired
	private MailUtil mailUtil;
	
	@PostMapping(value="{enUserNo}/{enFamilyNo}")
	 public ResponseEntity<Map<String,Object>> inviteInsert(@RequestHeader("accessAuthToken") String accessAuthToken,
			 												@RequestHeader("refreshAuthToken") String refreshAuthToken,
			 												@PathVariable String enUserNo,
			 												@PathVariable String enFamilyNo,
			 												@RequestBody InviteInfo inviteInfo){
		Map<String,Object> result = new HashMap<>();
		Boolean success = false;
		try {
			// 등록된 회원인지 확인
			UserInfo inviteEmailCheck = userInfoService.userCheck(inviteInfo.getEmail());
			if(inviteEmailCheck == null) {
				// 권한 체크
				int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
				if(jwtTokenProvider.validateToken(accessAuthToken)) {
					success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
				}else {
					success = jwtUserAuthCheck.userAuthCheck(refreshAuthToken , userNo);
				}
				if(Boolean.TRUE.equals(success)) {
					UserInfo userInfo = new UserInfo();
					userInfo.setUserNo(userNo);
					userInfo = userInfoService.detail(userInfo);
					
					// 가족 관리자 권한 체크
					int familyNo = Integer.parseInt(CryptoUtil.decode(enFamilyNo));		
					// !@#$ 일단 관리자만 회원 초대 가능
					success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyNo);
					if(Boolean.TRUE.equals(success)){
						// 초대 이메일 발송
						FamilyInfo familyInfo = new FamilyInfo();
						familyInfo.setFamilyNo(familyNo);
						familyInfo = familyInfoService.detail(familyInfo);
						String mailSubject = "dailyMoment에서 초대합니다";
						String mailBodySubject = familyInfo.getFamilyName()+"의 가족으로 초대합니다";
						String emailAuthCode = CryptoUtil.emailAuthCode();
						String mailBodyContent = "초대 코드 : "+emailAuthCode;
						success = mailUtil.sendMail(mailSubject,mailBodySubject ,mailBodyContent, inviteInfo);
						if(Boolean.TRUE.equals(success)) {
							inviteInfo.setFamilyNo(familyNo);
							inviteInfo.setAuthCode(emailAuthCode);
							inviteInfo.setIsDelete(0);
							inviteInfo.setIsPermission(0);
							inviteInfoService.insert(inviteInfo, userInfo);
						}
					}
				}
			}
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
		}catch(Exception e) {
			result.put("success",success);
			result.put("errorMsg", e.getCause().getMessage());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}	 
	}
	
	@GetMapping(value="{enUserNo}/{enFamilyNo}")
	 public ResponseEntity<Map<String,Object>> inviteList(@RequestHeader("accessAuthToken") String accessAuthToken,
			 												@RequestHeader("refreshAuthToken") String refreshAuthToken,
			 												@PathVariable String enUserNo,
			 												@PathVariable String enFamilyNo){
		Map<String,Object> result = new HashMap<>();
		Boolean success = false;
		try {
			// 권한 체크
			int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
			if(jwtTokenProvider.validateToken(accessAuthToken)) {
				success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
			}else {
				success = jwtUserAuthCheck.userAuthCheck(refreshAuthToken , userNo);
			}
			if(Boolean.TRUE.equals(success)) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNo(userNo);
				userInfo = userInfoService.detail(userInfo);
				
				// 가족 관리자 권한 체크
				int familyNo = Integer.parseInt(CryptoUtil.decode(enFamilyNo));		
				// !@#$ 일단 관리자만 회원 초대 가능
				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyNo);
				if(Boolean.TRUE.equals(success)){
					InviteInfoCriterion inviteInfoCriterion= new InviteInfoCriterion();
					inviteInfoCriterion.setSearchIsDelete(0);
					inviteInfoCriterion.setSearchIsPermission(0);
					inviteInfoCriterion.setSearchFamilyNo(familyNo);
					List<InviteInfo> inviteInfoList = inviteInfoService.list(inviteInfoCriterion);
					result.put("inviteInfoList", inviteInfoList);
				}
			}
			
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
		}catch(Exception e) {
			result.put("success",success);
			result.put("errorMsg", e.getCause().getMessage());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}	 
	}

	@PutMapping(value="{enUserNo}/{enInviteNo}")
	 public ResponseEntity<Map<String,Object>> inviteUpdate(@RequestHeader("accessAuthToken") String accessAuthToken,
			 												@RequestHeader("refreshAuthToken") String refreshAuthToken,
			 												@PathVariable String enUserNo,
			 												@PathVariable String enInviteNo,
			 												@RequestBody InviteInfo inviteInfo){
		Map<String,Object> result = new HashMap<>();
		Boolean success = false;
		try {			
			// 권한 체크
			int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
			if(jwtTokenProvider.validateToken(accessAuthToken)) {
				success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
			}else {
				success = jwtUserAuthCheck.userAuthCheck(refreshAuthToken , userNo);
			}
			if(Boolean.TRUE.equals(success)) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNo(userNo);
				userInfo = userInfoService.detail(userInfo);
				
				// 가족 관리자 권한 체크
				// !@#$ 일단 관리자만 회원 초대 가능
				int inviteNo = Integer.parseInt(CryptoUtil.decode(enInviteNo));
				InviteInfo updateInviteInfo = new InviteInfo();
				updateInviteInfo.setInviteNo(inviteNo);
				updateInviteInfo = inviteInfoService.detail(updateInviteInfo);
				
				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , updateInviteInfo.getFamilyNo());
				if(Boolean.TRUE.equals(success)){
					updateInviteInfo.setIsDelete(inviteInfo.getIsDelete());
					updateInviteInfo.setFamilyRelation(inviteInfo.getFamilyRelation());
					inviteInfoService.update(inviteInfo, userInfo);
				}
			}
			
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
		}catch(Exception e) {
			result.put("success",success);
			result.put("errorMsg", e.getCause().getMessage());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}	 
	}
	
	@PutMapping(value="inviteApprove")
	 public ResponseEntity<Map<String,Object>> inviteApprove(@RequestBody InviteInfo inviteInfo){
		Map<String,Object> result = new HashMap<>();
		Boolean success = false;
		try {
			InviteInfoCriterion inviteInfoCriterion = new InviteInfoCriterion();
			inviteInfoCriterion.setSearchAuthCode(inviteInfo.getAuthCode());
			inviteInfoCriterion.setSearchEmail(inviteInfo.getEmail());
			inviteInfoCriterion.setSearchIsDelete(0);
			inviteInfoCriterion.setSearchIsPermission(0);
			List<InviteInfo> inviteList = inviteInfoService.list(inviteInfoCriterion);
			// 일단 절대 안겹친다는 생각을 가지고 하지만 겹치는 경우가 생긴다면 일단 전부 승인 하는 형식
			// 이걸 안전성 있게 변경하려면 URL에서 inviteNo를 받는 형식으로 변경해야 할듯 한다.
			if(!inviteList.isEmpty()) {
				for(InviteInfo item : inviteList) {
					item.setIsPermission(1);
					inviteInfoService.inviteApprove(inviteInfo);
				}
				success = true;
			}
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
		}catch(Exception e) {
			result.put("success",success);
			result.put("errorMsg", e.getCause().getMessage());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}	 
	}
	
	
	
}
