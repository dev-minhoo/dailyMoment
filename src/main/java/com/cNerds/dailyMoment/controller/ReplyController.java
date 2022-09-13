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
import org.springframework.web.bind.annotation.RestController;

import com.cNerds.dailyMoment.core.jwt.JwtTokenProvider;
import com.cNerds.dailyMoment.core.jwt.JwtUserAuthCheck;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.photo.service.PhotoInfoService;
import com.cNerds.dailyMoment.reply.dto.ReplyInfo;
import com.cNerds.dailyMoment.reply.dto.ReplyInfoCriterion;
import com.cNerds.dailyMoment.reply.service.ReplyInfoService;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.service.UserInfoService;

@RestController
@RequestMapping("/reply/")
public class ReplyController {
	@Autowired
	private JwtUserAuthCheck jwtUserAuthCheck;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private PhotoInfoService photoInfoService;
	
	@Autowired	
	private ReplyInfoService replyInfoService;
	
	@PostMapping(value="/{enUserNo}")
    public ResponseEntity<Map<String,Object>> replyInsert(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@RequestHeader("refreshAuthToken") String refreshAuthToken,
    													@PathVariable String enUserNo,
    													@RequestBody ReplyInfo replyInfo
    													){
		Map<String,Object> result = new HashMap<>();
		Boolean success = true;

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
				int PhotoNo = replyInfo.getPhotoNo();
				
				int familyNo =  photoInfoService.searchPhotoFamilyNo(PhotoNo);
				
				success = jwtUserAuthCheck.familyMemberAuthCheck(userInfo , familyNo);
				if(Boolean.TRUE.equals(success)){				
					replyInfoService.insert(replyInfo,userInfo);								
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
			result.put("success",success);
			result.put("errorMsg", e.getCause().getMessage());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	@GetMapping(value="/{enUserNo}/{enPhotoNo}")
    public ResponseEntity<Map<String,Object>> replyList(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@RequestHeader("refreshAuthToken") String refreshAuthToken,
    													@PathVariable String enUserNo,
    													@PathVariable String enPhotoNo    
    													){
		Map<String,Object> result = new HashMap<>();
		Boolean success = true;

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
				int PhotoNo = Integer.parseInt(CryptoUtil.decode(enPhotoNo));
				
				int familyNo =  photoInfoService.searchPhotoFamilyNo(PhotoNo);
				
				success = jwtUserAuthCheck.familyMemberAuthCheck(userInfo , familyNo);
				if(Boolean.TRUE.equals(success)){				
					ReplyInfoCriterion replyInfoCriterion = new ReplyInfoCriterion();
					replyInfoCriterion.setSearchPhotoNo(PhotoNo);
					List<ReplyInfo> replyList = replyInfoService.list(replyInfoCriterion);
					result.put("replyList", replyList);
					
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
			result.put("success",success);
			result.put("errorMsg", e.getCause().getMessage());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	@PutMapping(value="/{enUserNo}/{enReply}")
    public ResponseEntity<Map<String,Object>> replyUpdate(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@RequestHeader("refreshAuthToken") String refreshAuthToken,
    													@PathVariable String enUserNo,
    													@PathVariable String enReply,
    													@RequestBody ReplyInfo replyInfo
    													){
		Map<String,Object> result = new HashMap<>();
		Boolean success = true;

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
				
				//권한 체크
				int replyNo = Integer.parseInt(CryptoUtil.decode(enReply));				
				
				success = jwtUserAuthCheck.replyAuthCheck(userInfo , replyNo);
				if(Boolean.TRUE.equals(success)){
					ReplyInfo updateReplyInfo = new ReplyInfo();
					updateReplyInfo.setReplyNo(replyNo);
					updateReplyInfo = replyInfoService.detail(updateReplyInfo);
					
					updateReplyInfo.setComment(replyInfo.getComment());
					replyInfoService.update(updateReplyInfo, userInfo);			
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
			result.put("success",success);
			result.put("errorMsg", e.getCause().getMessage());
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	@DeleteMapping(value="/{enUserNo}/{enReply}")
    public ResponseEntity<Map<String,Object>> replyDelete(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@RequestHeader("refreshAuthToken") String refreshAuthToken,
    													@PathVariable String enUserNo,
    													@PathVariable String enReplyNo
    													){
		Map<String,Object> result = new HashMap<>();
		Boolean success = true;

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
				
				int replyNo = Integer.parseInt(CryptoUtil.decode(enReplyNo));
				ReplyInfo deleteReplyInfo = new ReplyInfo();
				deleteReplyInfo.setReplyNo(replyNo);
				deleteReplyInfo = replyInfoService.detail(deleteReplyInfo);

//				int familyNo =  photoInfoService.searchPhotoFamilyNo(deleteReplyInfo.getPhotoNo());
				
//				// 관리자 여부 확인(맞나?!)
//				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyNo);
//				if(Boolean.TRUE.equals(success)) {
//					replyInfoService.delete(deleteReplyInfo);
//				}else {
					// 자기자신인지 확인
				success = jwtUserAuthCheck.replyAuthCheck(userInfo , replyNo);
				if(Boolean.TRUE.equals(success)){
					replyInfoService.delete(deleteReplyInfo);			
					}
//				}
				
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
	
}
