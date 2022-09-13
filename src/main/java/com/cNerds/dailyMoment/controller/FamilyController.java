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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cNerds.dailyMoment.core.jwt.JwtTokenProvider;
import com.cNerds.dailyMoment.core.jwt.JwtUserAuthCheck;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.family.dto.FamilyInfo;
import com.cNerds.dailyMoment.family.dto.FamilyInfoCriterion;
import com.cNerds.dailyMoment.family.service.FamilyInfoService;
import com.cNerds.dailyMoment.familyMember.dto.FamilyMemberInfo;
import com.cNerds.dailyMoment.familyMember.service.FamilyMemberInfoService;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.service.UserInfoService;


@RestController
@RequestMapping("/family/")
public class FamilyController {
	
	@Autowired
	private FamilyInfoService familyInfoService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private FamilyMemberInfoService familyMemberInfoService;
	
	@Autowired
	private JwtUserAuthCheck jwtUserAuthCheck;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@PostMapping(value="{enUserNo}")
    public ResponseEntity<Map<String,Object>> famliyInsert(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@RequestHeader("refreshAuthToken") String refreshAuthToken,
    													@PathVariable String enUserNo,
    													@RequestBody Map<String,String> body){
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
				FamilyInfo familyInfo = new FamilyInfo();
				familyInfo.setFamilyName(body.get("familyName"));
				familyInfoService.insert(familyInfo, userInfo);
				
				FamilyMemberInfo familyMemberInfo = new FamilyMemberInfo();
				familyMemberInfo.setFamilyNo(familyInfo.getFamilyNo());
				familyMemberInfo.setUserNo(userInfo.getUserNo());
				familyMemberInfo.setFamilyRelation(body.get("familyRelation"));
				familyMemberInfo.setIsFamilyAdmin(1);
				familyMemberInfo.setIsFamilyAgree(1);
				familyMemberInfoService.insert(familyMemberInfo, userInfo);
				
				result.put("familyInfo", familyInfo);
			}
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
			
		}catch (Exception e) {
			result.put("success",success);
			result.put("errorMsg", e.getCause().getMessage());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	@GetMapping(value="/{enUserNo}")
    public ResponseEntity <Map<String,Object>> familyList(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@RequestHeader("refreshAuthToken") String refreshAuthToken,
    													@PathVariable String enUserNo ){
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
				
				FamilyInfoCriterion familyInfoCriterion = new FamilyInfoCriterion();
				FamilyMemberInfo familyMemberInfo = new FamilyMemberInfo();
				familyMemberInfo.setUserNo(userInfo.getUserNo());
				familyInfoCriterion.setFamilyMemberInfo(familyMemberInfo);
				List<FamilyInfo> familList = familyInfoService.list(familyInfoCriterion);
				result.put("familyList", familList);
			}
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
	
		}catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getStackTrace());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	@PutMapping(value="/{enUserNo}/{enFamilyNo}")
    public ResponseEntity <Map<String,Object>> familyUpdate(@RequestHeader("accessAuthToken") String accessAuthToken,
    														@RequestHeader("refreshAuthToken") String refreshAuthToken,
    														@PathVariable String enUserNo,
    														@PathVariable String enFamilyNo,
    														@RequestBody FamilyInfo familyInfo){
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
				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyNo);
				if(Boolean.TRUE.equals(success)) {
					familyInfo.setFamilyNo(familyNo);
					familyInfoService.update(familyInfo, userInfo);
				}
			}
			
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
	
		}catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getStackTrace());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	// 가족 삭제
	@DeleteMapping(value="/{enUserNo}/{enFamilyNo}")
    public ResponseEntity <Map<String,Object>> familyDelete(@RequestHeader("accessAuthToken") String accessAuthToken,
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
				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyNo);
				if(Boolean.TRUE.equals(success)) {
					// 가족 권한 체크					
					FamilyInfo deleteFamilyInfo = new FamilyInfo();
					deleteFamilyInfo.setFamilyNo(familyNo);			
					familyInfoService.delete(deleteFamilyInfo);
					
					// !@#$ 나머지 하위 자료구조들 전부 삭제?
				
										
				}
			}
			
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
	
		}catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getStackTrace());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}		
	}
	

}