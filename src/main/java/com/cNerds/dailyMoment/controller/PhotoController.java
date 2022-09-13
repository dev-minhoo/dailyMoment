package com.cNerds.dailyMoment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cNerds.dailyMoment.children.dto.ChildrenInfo;
import com.cNerds.dailyMoment.children.service.ChildrenInfoService;
import com.cNerds.dailyMoment.core.jwt.JwtTokenProvider;
import com.cNerds.dailyMoment.core.jwt.JwtUserAuthCheck;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.core.util.FileUtil;
import com.cNerds.dailyMoment.photo.dto.PhotoInfo;
import com.cNerds.dailyMoment.photo.dto.PhotoInfoCriterion;
import com.cNerds.dailyMoment.photo.service.PhotoInfoService;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.service.UserInfoService;

@RestController
@RequestMapping("/photo/")
public class PhotoController {
		
		@Autowired
		private JwtUserAuthCheck jwtUserAuthCheck;
		
		@Autowired
		private JwtTokenProvider jwtTokenProvider;
	
		@Autowired
		private FileUtil fileService;
		
		@Autowired
		private UserInfoService userInfoService;
		
		@Autowired
		private ChildrenInfoService childrenInfoService;
		
		@Autowired
		private PhotoInfoService photoInfoService;
		
		@PostMapping(value = "/{enUserNo}/{enChildrenNo}", consumes = {"multipart/form-data"})
			public ResponseEntity<Map<String, Object>> photoInsert(@RequestHeader("accessAuthToken") String accessAuthToken,
																	@RequestHeader("refreshAuthToken") String refreshAuthToken,
																	@PathVariable String enUserNo,
																	@PathVariable String enChildrenNo,
																	@RequestPart(value="photoInfos" ,required = true)  List<PhotoInfo> photoInfos,
																	@RequestPart(value="photoImages", required = true) List<MultipartFile> photoImages) {
			
			Map<String, Object> result = new HashMap<>();
			Boolean success = false;
			int fileSaveType = 3;
			try {
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
					
					int childrenNo = Integer.parseInt(CryptoUtil.decode(enChildrenNo));
					ChildrenInfo childrenInfo = new ChildrenInfo();
					childrenInfo.setChildrenNo(childrenNo);
					childrenInfo = childrenInfoService.detail(childrenInfo);				
					String enFamilyNo = CryptoUtil.encode(Integer.toString(childrenInfo.getFamilyNo()));
					
					success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , childrenInfo.getFamilyNo());
					if(Boolean.TRUE.equals(success)) {
						if(photoInfos.size() == photoImages.size()) {
							for(int i = 0; i < photoInfos.size(); i++) {
								
								int maxPhotoNo = photoInfoService.searchMaxPhotoNo();
								String enPhotoNo = CryptoUtil.encode(Integer.toString(maxPhotoNo+1));
								String ext = FilenameUtils.getExtension(photoImages.get(i).getOriginalFilename());
								String fileName = enFamilyNo+"_"+enChildrenNo+"_"+enPhotoNo+"."+ext;
								
								success = fileService.saveFile(photoImages.get(i), fileSaveType, fileName);
								if(Boolean.TRUE.equals(success)) {
									// 이미지 db에 저장 할경우 변경
									photoInfos.get(i).setChildrenNo(childrenNo);
									photoInfos.get(i).setImageName(fileName);
									photoInfoService.insert(photoInfos.get(i), userInfo);
								}								
							}	
						}else {
							success = false;
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
			} catch (Exception e) {
				result.put("success", success);
				result.put("errorMsg", e.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
		}
		
		@GetMapping(value = "/{enUserNo}/{enChildrenNo}")
		public ResponseEntity<Map<String, Object>> photoList(@RequestHeader("accessAuthToken") String accessAuthToken,
															@RequestHeader("refreshAuthToken") String refreshAuthToken,
															@PathVariable String enUserNo,
															@PathVariable String enChildrenNo,
															@RequestBody PhotoInfoCriterion photoInfoCriterion) {
		
		Map<String, Object> result = new HashMap<>();
		Boolean success = false;
		try {
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
				
				int childrenNo = Integer.parseInt(CryptoUtil.decode(enChildrenNo));
				ChildrenInfo childrenInfo = new ChildrenInfo();
				childrenInfo.setChildrenNo(childrenNo);
				childrenInfo = childrenInfoService.detail(childrenInfo);				
				
				success = jwtUserAuthCheck.familyMemberAuthCheck(userInfo , childrenInfo.getFamilyNo());
				if(Boolean.TRUE.equals(success)) {
					List<PhotoInfo> photoList = photoInfoService.list(photoInfoCriterion);				
					result.put("photoList", photoList);
				}	
			}
			
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
		
		@GetMapping(value = "/{enUserNo}")
		public ResponseEntity<Map<String, Object>> likePhotoList(@RequestHeader("accessAuthToken") String accessAuthToken,
															@RequestHeader("refreshAuthToken") String refreshAuthToken,
															@PathVariable String enUserNo) {
		
		Map<String, Object> result = new HashMap<>();
		Boolean success = false;
		try {
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
				if(Boolean.TRUE.equals(success)) {
					PhotoInfoCriterion photoInfoCriterion = new PhotoInfoCriterion();
					photoInfoCriterion.setSearchLikePhotoUserNo(userNo);
					List<PhotoInfo> photoList = photoInfoService.list(photoInfoCriterion);				
					result.put("photoList", photoList);
				}	
			}
			
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
		
		@PutMapping(value = "/{enUserNo}/{enPhotoNo}", consumes = {"multipart/form-data"})
		public ResponseEntity<Map<String, Object>> photoUpdate(@RequestHeader("accessAuthToken") String accessAuthToken,
																@RequestHeader("refreshAuthToken") String refreshAuthToken,
																@PathVariable String enUserNo,
																@PathVariable String enPhotoNo,
																@RequestPart(value="photoInfo" ,required = true)   PhotoInfo photoInfo,
																@RequestPart(value="photoImage", required = true) MultipartFile photoImage) {
		
		Map<String, Object> result = new HashMap<>();
		Boolean success = false;
		int fileSaveType = 3;
		try {
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
				
				int photoNo = Integer.parseInt(CryptoUtil.decode(enPhotoNo));
				int familyNo = photoInfoService.searchPhotoFamilyNo(photoNo);
				
				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyNo);
				
				if(Boolean.TRUE.equals(success)) {	
					// !@#$ 이미지 파일 파일 명 저장 여부 
	
					photoInfo.setPhotoNo(photoNo);
					PhotoInfo updatePhotoInfo = photoInfoService.detail(photoInfo);
					String ext = FilenameUtils.getExtension(photoImage.getOriginalFilename());
					String fileName = CryptoUtil.encode(Integer.toString(familyNo))
									+"_"+CryptoUtil.encode(Integer.toString(updatePhotoInfo.getChildrenNo()))+"_"
									+enPhotoNo+"." + ext;
					success = fileService.saveFile(photoImage, fileSaveType, fileName);
					if(Boolean.TRUE.equals(success)) {
						updatePhotoInfo.setImageName(fileName);
						updatePhotoInfo.setPhotoDate(photoInfo.getPhotoDate());
						updatePhotoInfo.setLatitude(photoInfo.getLatitude());
						updatePhotoInfo.setHardness(photoInfo.getHardness());
						updatePhotoInfo.setIsMain(photoInfo.getIsMain());
						updatePhotoInfo.setComment(photoInfo.getComment());
						photoInfoService.update(updatePhotoInfo, userInfo);
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
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
		
		@DeleteMapping(value = "/{enUserNo}/{enPhotoNo}")
		public ResponseEntity<Map<String, Object>> photoDelete(@RequestHeader("accessAuthToken") String accessAuthToken,
																@RequestHeader("refreshAuthToken") String refreshAuthToken,
																@PathVariable String enUserNo,
																@PathVariable String enPhotoNo) {
		
		Map<String, Object> result = new HashMap<>();
		Boolean success = false;
		try {
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
				
				int photoNo = Integer.parseInt(CryptoUtil.decode(enPhotoNo));
				int familyNo = photoInfoService.searchPhotoFamilyNo(photoNo);
				
				success = jwtUserAuthCheck.familyAdminAuthCheck(userInfo , familyNo);			
				if(Boolean.TRUE.equals(success)) {
					PhotoInfo deletePhotoInfo = new PhotoInfo();
					deletePhotoInfo.setPhotoNo(photoNo);
					photoInfoService.delete(deletePhotoInfo);
				}	
			}	
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
}
