package com.cNerds.dailyMoment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cNerds.dailyMoment.children.dto.ChildrenInfo;
import com.cNerds.dailyMoment.children.dto.ChildrenInfoCriterion;
import com.cNerds.dailyMoment.children.service.ChildrenInfoService;
import com.cNerds.dailyMoment.core.jwt.JwtUserAuthCheck;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.core.util.FileUtil;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.service.UserInfoService;


@RestController
@RequestMapping("/children/")
public class ChildrenController {
	
	@Autowired
	private JwtUserAuthCheck jwtUserAuthCheck;
	
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ChildrenInfoService childrenInfoService;
	
	@Autowired
	private FileUtil fileService;
	
	
	@PostMapping(value="/{enUserNo}/{enFamilyNo}", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String,Object>> childrenInsert(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@PathVariable String enUserNo,
    													@PathVariable String enFamilyNo,
    													@RequestPart(value="childrenInfo" ,required = true) ChildrenInfo childrenInfo,
    													@RequestPart(value="childrenProfileImage", required = false) MultipartFile childrenProfileImage
    													){
		Map<String,Object> result = new HashMap<>();
		Boolean success = false;
		int fileSaveType = 2;
		try {
			// 권한 체크
			int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
			success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
			if(Boolean.TRUE.equals(success)) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNo(userNo);
				userInfo = userInfoService.detail(userInfo);
				
				// 가족 관리자 권한 체크
				int familyNo = Integer.parseInt(CryptoUtil.decode(enFamilyNo));
				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyNo);
				if(Boolean.TRUE.equals(success)){
					String fileName = null;
					if(childrenProfileImage != null) {
						int maxChildrenNo = childrenInfoService.searchMaxChildrenNo();
						String enChildrenNo = CryptoUtil.encode(Integer.toString(maxChildrenNo+1));
						String ext = FilenameUtils.getExtension(childrenProfileImage.getOriginalFilename());
						fileName = enFamilyNo+"_"+enChildrenNo+"."+ext;
						success = fileService.saveFile(childrenProfileImage, fileSaveType, fileName);
					}
					childrenInfo.setFamilyNo(familyNo);
					childrenInfo.setImageName(fileName);
					childrenInfoService.insert(childrenInfo,userInfo);
					
					
						
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
	
	@GetMapping(value="/{enUserNo}/{enFamilyNo}")
    public ResponseEntity<Map<String,Object>> childrenList(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@PathVariable String enUserNo,
    													@PathVariable String enFamilyNo){
		Map<String,Object> result = new HashMap<>();
		Boolean success = false;
		try {
			
			// 권한 체크
			int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
			success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
			if(Boolean.TRUE.equals(success)) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNo(userNo);
				userInfo = userInfoService.detail(userInfo);
				
				// 가족 권한 체크
				int familyNo = Integer.parseInt(CryptoUtil.decode(enFamilyNo));
				success = jwtUserAuthCheck.familyMemberAuthCheck(userInfo , familyNo);
				if(Boolean.TRUE.equals(success)){					
					ChildrenInfoCriterion chidlrenInfoCriterion = new ChildrenInfoCriterion();
					chidlrenInfoCriterion.setSearchfamilyNo(familyNo);
					List<ChildrenInfo> childrenList = childrenInfoService.list(chidlrenInfoCriterion);					
					result.put("childrenList", childrenList);
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
	
	@PutMapping(value="/{enUserNo}/{enChildrenNo}" , consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String,Object>> childrenUpdate(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@PathVariable String enUserNo,
    													@PathVariable String enChildrenNo,
    													@RequestPart(value="childrenInfo" ,required = true) ChildrenInfo childrenInfo,
    													@RequestPart(value="childrenProfileImage", required = false) MultipartFile childrenProfileImage
    													){
		Map<String,Object> result = new HashMap<>();
		Boolean success = false;
		int fileSaveType = 2;
		try {
			
			// 권한 체크
			int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
			success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
			if(Boolean.TRUE.equals(success)) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNo(userNo);
				userInfo = userInfoService.detail(userInfo);
				
				
				
				ChildrenInfo updateChildrenInfo = new ChildrenInfo();
				int childrenNo = Integer.parseInt(CryptoUtil.decode(enChildrenNo));
				updateChildrenInfo.setChildrenNo(childrenNo);
				updateChildrenInfo = childrenInfoService.detail(updateChildrenInfo);
				//update 아이 정보 확인
				if(updateChildrenInfo != null) {
					// 가족 관리자 권한 체크
					success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , updateChildrenInfo.getFamilyNo());
					if(Boolean.TRUE.equals(success)){
						String fileName = null;
						String enFamilyNo = CryptoUtil.encode(Integer.toString(updateChildrenInfo.getFamilyNo()));
						
						if(childrenProfileImage != null) {
							String ext = FilenameUtils.getExtension(childrenProfileImage.getOriginalFilename());
							fileName = enFamilyNo+"_"+enChildrenNo+"."+ext;
							success = fileService.saveFile(childrenProfileImage, fileSaveType, fileName);
						}						
						updateChildrenInfo.setChildrenName(childrenInfo.getChildrenName());
						updateChildrenInfo.setChildrenBirth(childrenInfo.getChildrenBirth());
						updateChildrenInfo.setChildrenGender(childrenInfo.getChildrenGender());
						updateChildrenInfo.setWeight(childrenInfo.getWeight());
						updateChildrenInfo.setHeight(childrenInfo.getHeight());
						updateChildrenInfo.setNewBornBabyName(childrenInfo.getNewBornBabyName());
						updateChildrenInfo.setImageName(fileName);
						childrenInfoService.update(updateChildrenInfo,userInfo);								
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
	
	@DeleteMapping(value="/{enUserNo}/{enChildrenNo}")
    public ResponseEntity<Map<String,Object>> childrenDelete(@RequestHeader("accessAuthToken") String accessAuthToken,
    													@PathVariable String enUserNo,
    													@PathVariable String enChildrenNo
    													){
		Map<String,Object> result = new HashMap<>();
		Boolean success = false;
		try {
			
			// 권한 체크
			int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
			success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
			if(Boolean.TRUE.equals(success)) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNo(userNo);
				userInfo = userInfoService.detail(userInfo);

				// 가족 관리자 권한 체크
				int childrenNo = Integer.parseInt(CryptoUtil.decode(enChildrenNo));
				ChildrenInfo childrenInfo = new ChildrenInfo();
				childrenInfo.setChildrenNo(childrenNo);
				childrenInfo = childrenInfoService.detail(childrenInfo);
				
				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , childrenInfo.getFamilyNo());
				if(Boolean.TRUE.equals(success)){
					// !@# 하위구조 delete 처리
					childrenInfoService.delete(childrenInfo);								
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
