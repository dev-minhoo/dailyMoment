package com.cNerds.dailyMoment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cNerds.dailyMoment.core.jwt.JwtTokenProvider;
import com.cNerds.dailyMoment.core.jwt.JwtUserAuthCheck;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.like.dto.LikeInfo;
import com.cNerds.dailyMoment.like.dto.LikeInfoCriterion;
import com.cNerds.dailyMoment.like.service.LikeInfoService;
import com.cNerds.dailyMoment.photo.service.PhotoInfoService;
import com.cNerds.dailyMoment.reply.dto.ReplyInfo;
import com.cNerds.dailyMoment.reply.service.ReplyInfoService;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.service.UserInfoService;

@RestController
@RequestMapping("/like/")
public class LikeController {
	
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
	
	@Autowired
	private LikeInfoService likeInfoService;
	
	@PostMapping(value="/{enUserNo}")
    public ResponseEntity<Map<String,Object>> replyInsert(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@RequestHeader("refreshAuthToken") String refreshAuthToken,
    													@PathVariable String enUserNo,
    													@RequestBody LikeInfo likeInfo
    													){
		Map<String,Object> result = new HashMap<>();
		Boolean success = true;
		String insertType = "";

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
				
				// 권한체크
				if(likeInfo.getPhotoNo() != 0) {
					int familyNo =  photoInfoService.searchPhotoFamilyNo(likeInfo.getPhotoNo());
					success = jwtUserAuthCheck.familyMemberAuthCheck(userInfo , familyNo);
					if(Boolean.TRUE.equals(success)){	
						// 중복 체크
						LikeInfoCriterion likeInfoCriterion = new LikeInfoCriterion();
						likeInfoCriterion.setSearchPhotoNo(likeInfo.getPhotoNo());
						likeInfoCriterion.setSearchUserNo(userInfo.getUserNo());
						List<LikeInfo>LikeInfoList = likeInfoService.list(likeInfoCriterion);
						if(LikeInfoList.isEmpty()) {
							likeInfoService.insert(likeInfo,userInfo);
						}									
					}
				}else if(likeInfo.getReplyNo() != 0) {
					int familyNo =  replyInfoService.searchReplyFamilyNo(likeInfo.getReplyNo());
					success = jwtUserAuthCheck.familyMemberAuthCheck(userInfo , familyNo);
					if(Boolean.TRUE.equals(success)){
						// 중복 체크
						LikeInfoCriterion likeInfoCriterion = new LikeInfoCriterion();
						likeInfoCriterion.setSearchReplyNo(likeInfo.getReplyNo());
						likeInfoCriterion.setSearchUserNo(userInfo.getUserNo());
						List<LikeInfo>LikeInfoList = likeInfoService.list(likeInfoCriterion);
						if(LikeInfoList.isEmpty()) {
							likeInfoService.insert(likeInfo,userInfo);
						}													
					}
				}else {
					success = false;
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
	
	@DeleteMapping(value="/{enUserNo}/{enLikeNo}")
    public ResponseEntity<Map<String,Object>> replyDelete(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@RequestHeader("refreshAuthToken") String refreshAuthToken,
    													@PathVariable String enUserNo,
    													@PathVariable String enLikeNo){
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
				
				// 권한 체크
				int replyNo = Integer.parseInt(CryptoUtil.decode(enLikeNo));				
				
				success = jwtUserAuthCheck.likeAuthCheck(userInfo , replyNo);
				if(Boolean.TRUE.equals(success)){
					ReplyInfo deleteReplyInfo = new ReplyInfo();
					deleteReplyInfo.setReplyNo(replyNo);
					replyInfoService.delete(deleteReplyInfo);			
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
	
	
	
}
