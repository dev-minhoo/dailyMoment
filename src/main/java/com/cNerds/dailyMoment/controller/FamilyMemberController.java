package com.cNerds.dailyMoment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cNerds.dailyMoment.core.jwt.JwtTokenProvider;
import com.cNerds.dailyMoment.core.jwt.JwtUserAuthCheck;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfoCriterion;
import com.cNerds.dailyMoment.familyMember.service.FamilyMemberInfoService;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.dto.UserInfoCriterion;
import com.cNerds.dailyMoment.user.service.UserInfoService;

@RestController
@RequestMapping("/familyMember/")
public class FamilyMemberController {
	
	@Autowired
	private JwtUserAuthCheck jwtUserAuthCheck;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private FamilyMemberInfoService familyMemberInfoService;
	
	@PostMapping(value="{enUserNo}/{enFamilyNo}")
	 public ResponseEntity<Map<String,Object>> familyMemberInsert(@RequestHeader("accessAuthToken") String accessAuthToken,
			 												@RequestHeader("refreshAuthToken") String refreshAuthToken,
			 												@PathVariable String enUserNo,
			 												@PathVariable String enFamilyNo,
			 												@RequestBody FamilyMemberInfo familyMemberInfo){
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
						familyMemberInfo.setFamilyNo(familyNo);
						familyMemberInfoService.insert(familyMemberInfo, userInfo);
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
	
	// 가족인원 보기
	@GetMapping(value="{enUserNo}/{enFamilyNo}")
	 public ResponseEntity<Map<String,Object>> familyMemberList(@RequestHeader("accessAuthToken") String accessAuthToken,
			 												@RequestHeader("refreshAuthToken") String refreshAuthToken,
			 												@PathVariable String enUserNo,
			 												@PathVariable String enFamilyNo,
			 												@RequestBody FamilyMemberInfoCriterion familyMemberInfoCriterion){
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
					
					// 가족 권한 체크
					int familyNo = Integer.parseInt(CryptoUtil.decode(enFamilyNo));		
					// !@#$ 일단 관리자만 회원 초대 가능
					success = jwtUserAuthCheck.familyMemberAuthCheck(userInfo , familyNo);
					if(Boolean.TRUE.equals(success)){
						familyMemberInfoCriterion.setSearchFamilyNo(familyNo);
						List<FamilyMemberInfo>familyMemberList = familyMemberInfoService.list(familyMemberInfoCriterion);
						result.put("familyMemberList", familyMemberList);	
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
	
	// 초대 확인 화면
	@GetMapping(value="{enUserNo}")
	 public ResponseEntity<Map<String,Object>> familyMemberInviteList(@RequestHeader("accessAuthToken") String accessAuthToken,
			 														@RequestHeader("refreshAuthToken") String refreshAuthToken,
			 														@PathVariable String enUserNo){
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
					FamilyMemberInfoCriterion familyMemberInfoCriterion = new FamilyMemberInfoCriterion();
					familyMemberInfoCriterion.setSearchUserNo(userNo);
					familyMemberInfoCriterion.setSearchIsFamilyAgree(0);
					List<FamilyMemberInfo>familyMemberList = familyMemberInfoService.list(familyMemberInfoCriterion);
					result.put("familyMemberInviteList", familyMemberList);	
	
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
	
	@PutMapping(value="{enUserNo}/{enFamilyMemberNo}")
	 public ResponseEntity<Map<String,Object>> familyMemberUpdate(@RequestHeader("accessAuthToken") String accessAuthToken,
			 												@RequestHeader("refreshAuthToken") String refreshAuthToken,
			 												@PathVariable String enUserNo,
			 												@PathVariable String enFamilyMemberNo,
			 												@RequestBody FamilyMemberInfo familyMemberInfo){
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
					int familyMemberNo = Integer.parseInt(CryptoUtil.decode(enFamilyMemberNo));
					FamilyMemberInfo updateFamilyMemberInfo = new FamilyMemberInfo();
					updateFamilyMemberInfo.setFamilyMemberNo(familyMemberNo);
					updateFamilyMemberInfo = familyMemberInfoService.detail(familyMemberInfo);
					
					success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , updateFamilyMemberInfo.getFamilyNo());
					if(Boolean.TRUE.equals(success)){
						updateFamilyMemberInfo.setFamilyRelation(familyMemberInfo.getFamilyRelation());
						familyMemberInfoService.update(familyMemberInfo, userInfo);
					}else {
						// 본인일시 (수락)
						if(userNo == familyMemberInfo.getFamilyNo()) {
							updateFamilyMemberInfo.setFamilyRelation(familyMemberInfo.getFamilyRelation());
							updateFamilyMemberInfo.setIsFamilyAgree(familyMemberInfo.getIsFamilyAgree());
							success = true;
							familyMemberInfoService.update(familyMemberInfo, userInfo);
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
	
	@DeleteMapping(value="{enUserNo}/{enFamilyMemberNo}")
	 public ResponseEntity<Map<String,Object>> familyMemberDelete(@RequestHeader("accessAuthToken") String accessAuthToken,
			 												@RequestHeader("refreshAuthToken") String refreshAuthToken,
			 												@PathVariable String enUserNo,
			 												@PathVariable String enFamilyMemberNo){
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
					
					FamilyMemberInfo familyMemberInfo = new FamilyMemberInfo();
					familyMemberInfo.setFamilyMemberNo(Integer.parseInt(CryptoUtil.decode(enFamilyMemberNo)));
					familyMemberInfo = familyMemberInfoService.detail(familyMemberInfo);
					
					// 가족 관리자 권한 체크
						
					success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyMemberInfo.getFamilyNo());
					if(Boolean.TRUE.equals(success)){
						familyMemberInfoService.delete(familyMemberInfo);
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
	
	
	// 초대할때 회원 리스트
	@GetMapping(value="{enUserNo}/userList")
	public ResponseEntity<Map<String, Object>> userList(@RequestHeader("accessAuthToken") String accessAuthToken,
														@RequestHeader("refreshAuthToken") String refreshAuthToken,
														@PathVariable String enUserNo,
														@RequestBody UserInfo userInfo){
		Map<String, Object> result = new HashMap<>();
		Boolean success = false;
		try {
				int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
				// 권한 체크
				if(jwtTokenProvider.validateToken(accessAuthToken)) {
					success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
				}else {
					success = jwtUserAuthCheck.userAuthCheck(refreshAuthToken , userNo);
				}
				if (Boolean.TRUE.equals(success)) {
					UserInfoCriterion userInfoCriterion = new UserInfoCriterion();
					if(userInfo.getUserId() != null && !userInfo.getUserId().equals("")) {
						userInfoCriterion.setSearchUserId(userInfo.getUserId());
					}
					List<UserInfo> userList = userInfoService.list(userInfoCriterion);
					result.put("inviteUserList", userList);
				}
			
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
		}catch(Exception e){
			result.put("success", success);
			result.put("errorMsg", e.getCause().getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		
	}
	
}
