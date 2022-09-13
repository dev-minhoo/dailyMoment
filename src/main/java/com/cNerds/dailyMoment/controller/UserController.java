package com.cNerds.dailyMoment.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cNerds.dailyMoment.core.jwt.JwtAuthToken;
import com.cNerds.dailyMoment.core.jwt.JwtTokenProvider;
import com.cNerds.dailyMoment.core.jwt.JwtUserAuthCheck;
import com.cNerds.dailyMoment.core.jwt.SocialAuthToken;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.core.util.FileUtil;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.dto.UserInfoCriterion;
import com.cNerds.dailyMoment.user.service.UserInfoService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private JwtUserAuthCheck jwtUserAuthCheck;

	@Autowired
	private FileUtil fileService;
	
	public String kakaoLoginUrl ="https://kauth.kakao.com/oauth/authorize?";
	public String kakaoClientId ="";
	public String kakaoRedirectUrl ="http://localhost:8080/user/auth/authKakao";
	public String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
	public String kakaoSocialInfoUrl ="https://kapi.kakao.com/v2/user/me";
	
	public String naverLoginUrl ="https://nid.naver.com/oauth2.0/authorize?";
	public String naverClientId ="";
	public String naverSecretKey ="";
	public String naverRedirectUrl ="http://localhost:8080/user/auth/authNaver";
	public String naverTokenUrl = "https://nid.naver.com/oauth2.0/token";
	public String naverSocialInfoUrl ="https://openapi.naver.com/v1/nid/me";
	
	public String googleLoginUrl ="https://accounts.google.com/o/oauth2/v2/auth?";
	public String googleClientId ="";
	public String googleSecretKey ="";
	public String googleRedirectUrl ="http://localhost:8080/user/auth/authGoogle";
	public String googleTokenUrl = "https://oauth2.googleapis.com/token";
	public String googleSocialInfoUrl ="https://www.googleapis.com/drive/v2/files";

	@PostMapping(value = "/auth/signUp")
	public ResponseEntity<Map<String, Object>> createMember(@RequestBody UserInfo signUpUserInfo) {
		Boolean success = false;
		Map<String, Object> result = new HashMap<>();

		try {
			signUpUserInfo.setUserPwd(CryptoUtil.getHashedPassword(signUpUserInfo.getUserPwd()));
			signUpUserInfo.setIsSocial(0);
			userInfoService.insert(signUpUserInfo, signUpUserInfo);
			success = true;
			result.put("success", success);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getCause().getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	@GetMapping(value = "/auth/validIdCheck")
	public ResponseEntity<Map<String, Object>> validIdCheck(@RequestBody UserInfo validCheckUserInfo) {
		Boolean success = false;
		Map<String, Object> result = new HashMap<>();

		try {
			UserInfoCriterion userInfoCriterion = new UserInfoCriterion();
			userInfoCriterion.setSearchUserId(validCheckUserInfo.getUserId());
			List<UserInfo> validCheckUser =	userInfoService.list(userInfoCriterion);
			if(validCheckUser.isEmpty()) 
				result.put("isValid", true);
			else 
				result.put("isValid", false);
			
				
			success = true;
			result.put("success", success);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getCause().getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	

	@PostMapping(value = "/auth/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody UserInfo logInUserInfo) {
		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		Boolean success = false;
		String role = "test";
		try {
			// 아이디 확인
			UserInfo userCheck = userInfoService.userCheck(logInUserInfo.getUserId());
			// 비밀번호 체크
			if (userCheck != null) {
				if (CryptoUtil.isEqual(logInUserInfo.getUserPwd(), userCheck.getUserPwd())) {
					JwtAuthToken token = jwtTokenProvider.createToken(userCheck.getUserId(), role);
					userCheck.setUserAuthCheckToken(token.getRefreshAuthToken());
					userInfoService.updateAuthToken(userCheck, userCheck);			
					userCheck.setUserAuthCheckToken(null);

					success = true;
					responseHeaders.set("accessAuthToken", token.getAccessAuthToken());
					responseHeaders.set("refreshAuthToken", token.getRefreshAuthToken());
					result.put("userInfo", userCheck);
				}
			}
			if (Boolean.TRUE.equals(success)) {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(result);
			} else {
				result.put("success", success);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
			}

		} catch (Exception e) {

			result.put("success", success);
			result.put("errorMsg", e.getCause().getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}

	@GetMapping(value = "/auth/kakaoLogin")
	public ResponseEntity<Map<String, Object>> kakaoLogin() {
		Map<String, Object> result = new HashMap<>();
		String reqUrl = kakaoLoginUrl + "client_id="+kakaoClientId
				+ "&redirect_uri="+ kakaoRedirectUrl + "&response_type=code";
		result.put("reqUrl", reqUrl);

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@GetMapping(value = "/auth/authKakao")
	public ResponseEntity<Map<String, Object>> authKakao(@RequestParam String code) {
		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		String socialType = "kakao";
		Boolean success = false;
		String role = "test";
		try {
			// 토큰 가져오기
			SocialAuthToken socialAuthToken = this.socialCreateToken(socialType, code);

			// 토큰정보 가져오기
			socialAuthToken = this.getSocialUserInfo(socialAuthToken, socialType);

			// 유저 정보 확인
			UserInfo userCheck = userInfoService.userCheck(socialAuthToken.getSocialEmail());
			// userCheck.getIsAgree()
			if (userCheck != null && userCheck.getIsAgree() == 1) {
				JwtAuthToken token = jwtTokenProvider.createToken(userCheck.getUserId(), role);
				userCheck.setUserAuthCheckToken(token.getRefreshAuthToken());
				userInfoService.updateAuthToken(userCheck, userCheck);
				userCheck.setUserAuthCheckToken(null);

				success = true;
				result.put("success", success);
				responseHeaders.set("accessAuthToken", token.getAccessAuthToken());
				responseHeaders.set("refreshAuthToken", token.getRefreshAuthToken());
				result.put("userInfo", userCheck);

				return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(result);
			} else {
				JsonObject userInfo = new JsonObject();
				if (userCheck == null) {
					userCheck = new UserInfo();
					userCheck.setUserId(socialAuthToken.getSocialEmail());
					userCheck.setIsSocial(1);
					userInfoService.insert(userCheck, userCheck);
				}
				userCheck.setEnUserNo(CryptoUtil.encode(Integer.toString(userCheck.getUserNo())));
				result.put("success", success);
				result.put("userInfo", userCheck);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			}
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getCause().getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}

	}

	@GetMapping(value = "/auth/naverLogin")
	public ResponseEntity<Map<String, Object>> naverLogin() {
		Map<String, Object> result = new HashMap<>();
		String reqUrl =  naverLoginUrl+ "client_id="+naverClientId
				+ "&redirect_uri="+naverRedirectUrl + "&response_type=code"
				+ "&state=hLiDdL2uhPtsftcU";

		result.put("reqUrl", reqUrl);

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@GetMapping(value = "/auth/authNaver")
	public ResponseEntity<Map<String, Object>> authNaver(@RequestParam String code) {
		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		String socialType = "naver";
		Boolean success = false;
		String role = "test";
		try {
			// 토큰 가져오기
			SocialAuthToken socialAuthToken = this.socialCreateToken(socialType, code);

			// 토큰정보 가져오기
			socialAuthToken = this.getSocialUserInfo(socialAuthToken, socialType);

			// 유저 정보 확인
			UserInfo userCheck = userInfoService.userCheck(socialAuthToken.getSocialEmail());

			if (userCheck != null && userCheck.getIsAgree() == 1) {
				JwtAuthToken token = jwtTokenProvider.createToken(userCheck.getUserId(), role);
				userCheck.setUserAuthCheckToken(token.getRefreshAuthToken());
				userInfoService.updateAuthToken(userCheck, userCheck);
				userCheck.setUserAuthCheckToken(null);
				
				success = true;
				result.put("success", success);
				responseHeaders.set("accessAuthToken", token.getAccessAuthToken());
				responseHeaders.set("refreshAuthToken", token.getRefreshAuthToken());
				result.put("userInfo", userCheck);

				return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(result);
			} else {

				if (userCheck == null) {
					userCheck = new UserInfo();
					userCheck.setUserId(socialAuthToken.getSocialEmail());
					userCheck.setIsSocial(2);
					userInfoService.insert(userCheck, userCheck);
				}
				userCheck.setEnUserNo(CryptoUtil.encode(Integer.toString(userCheck.getUserNo())));
				success = true;
				result.put("success", success);
				result.put("userInfo", userCheck);

				return ResponseEntity.status(HttpStatus.OK).body(result);
			}
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getCause().getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	
	@GetMapping(value = "/auth/googleLogin")
	public ResponseEntity<Map<String, Object>> googleLogin() {
		Map<String, Object> result = new HashMap<>();
		
		String reqUrl = googleLoginUrl
			 + "scope=https%3A//www.googleapis.com/auth/drive.metadata.readonly"
			 + "&access_type=offline"
			 + "&include_granted_scopes=true"
			 + "&response_type=code"
			 //+ "&state=state_parameter_passthrough_value"
			 + "&redirect_uri=" + googleRedirectUrl
			 + "&client_id="+ googleClientId;
		result.put("reqUrl", reqUrl);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@GetMapping(value = "/auth/authGoogle")
	public ResponseEntity<Map<String, Object>> authGoogle(@RequestParam String code) {
		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		String socialType = "google";
		Boolean success = false;
		String role = "test";
		try {
			// 토큰 가져오기
			SocialAuthToken socialAuthToken = this.socialCreateToken(socialType, code);

			// 토큰정보 가져오기
			socialAuthToken = this.getSocialUserInfo(socialAuthToken, socialType);

			// 유저 정보 확인
			UserInfo userCheck = userInfoService.userCheck(socialAuthToken.getSocialEmail());
			// userCheck.getIsAgree()
			if (userCheck != null && userCheck.getIsAgree() == 1) {
				JwtAuthToken token = jwtTokenProvider.createToken(userCheck.getUserId(), role);
				userCheck.setUserAuthCheckToken(token.getRefreshAuthToken());
				userInfoService.updateAuthToken(userCheck, userCheck);
				userCheck.setUserAuthCheckToken(null);

				success = true;
				result.put("success", success);
				responseHeaders.set("accessAuthToken", token.getAccessAuthToken());
				responseHeaders.set("refreshAuthToken", token.getRefreshAuthToken());
				result.put("userInfo", userCheck);

				return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(result);
			} else {
				JsonObject userInfo = new JsonObject();
				if (userCheck == null) {
					userCheck = new UserInfo();
					userCheck.setUserId(socialAuthToken.getSocialEmail());
					userCheck.setIsSocial(1);
					userInfoService.insert(userCheck, userCheck);
				}
				userCheck.setEnUserNo(CryptoUtil.encode(Integer.toString(userCheck.getUserNo())));
				result.put("success", success);
				result.put("userInfo", userCheck);
				return ResponseEntity.status(HttpStatus.OK).body(result);
			}
		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getCause().getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}

	}

	private SocialAuthToken socialCreateToken(String socialType, String code) {
		SocialAuthToken socialAuthToken = new SocialAuthToken();
		if (socialType.equals("kakao")) {
			try {
				URL url = new URL(kakaoTokenUrl);

				HttpURLConnection hConn = (HttpURLConnection) url.openConnection();
				hConn.setRequestMethod("POST");
				hConn.setDoOutput(true);

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(hConn.getOutputStream()));
				StringBuilder sb = new StringBuilder();
				sb.append("grant_type=authorization_code");
				sb.append("&client_id="+kakaoClientId); // 본인이 발급받은 key
				sb.append("&redirect_uri="+kakaoRedirectUrl); // 본인이 설정해 놓은 경로
				sb.append("&code=" + code);
				bw.write(sb.toString());
				bw.flush();
				// 결과 코드가 200이라면 성공
				int responseCode = hConn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(hConn.getInputStream()));
				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				// Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);

				socialAuthToken.setSoAccessAuthToken(element.getAsJsonObject().get("access_token").getAsString());
				socialAuthToken.setSoRefreshAuthToken(element.getAsJsonObject().get("refresh_token").getAsString());

				br.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (socialType.equals("naver")) {
			try {
				URL url = new URL(naverTokenUrl);

				HttpURLConnection hConn = (HttpURLConnection) url.openConnection();
				hConn.setRequestMethod("POST");
				hConn.setDoOutput(true);

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(hConn.getOutputStream()));
				StringBuilder sb = new StringBuilder();
				sb.append("grant_type=authorization_code");
				sb.append("&client_id="+naverClientId); // 본인이 발급받은 key
				sb.append("&client_secret="+naverSecretKey); // 본인이 설정해 놓은 경로
				sb.append("&code=" + code);
				bw.write(sb.toString());
				bw.flush();
				// 결과 코드가 200이라면 성공
				int responseCode = hConn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(hConn.getInputStream()));
				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				// Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);

				socialAuthToken.setSoAccessAuthToken(element.getAsJsonObject().get("access_token").getAsString());
				socialAuthToken.setSoRefreshAuthToken(element.getAsJsonObject().get("refresh_token").getAsString());

				br.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(socialType.equals("google")) {
			try {
				URL url = new URL(googleTokenUrl);

				HttpURLConnection hConn = (HttpURLConnection) url.openConnection();
				hConn.setRequestMethod("POST");
				hConn.setDoOutput(true);

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(hConn.getOutputStream()));
				StringBuilder sb = new StringBuilder();
				sb.append("grant_type=authorization_code");
				sb.append("&client_id="+googleClientId); // 본인이 발급받은 key
				sb.append("&client_secret="+googleSecretKey); 
				sb.append("&redirect_uri="+googleRedirectUrl); // 본인이 설정해 놓은 경로
				sb.append("&code=" + code);
				bw.write(sb.toString());
				bw.flush();
				// 결과 코드가 200이라면 성공
				int responseCode = hConn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(hConn.getInputStream()));
				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				// Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);

				socialAuthToken.setSoAccessAuthToken(element.getAsJsonObject().get("access_token").getAsString());
				socialAuthToken.setSoRefreshAuthToken(element.getAsJsonObject().get("refresh_token").getAsString());

				br.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return socialAuthToken;
	}

	private SocialAuthToken getSocialUserInfo(SocialAuthToken socialAuthToken, String socialType) {
		if (socialType.equals("kakao")) {
			try {
				URL url = new URL(kakaoSocialInfoUrl);

				HttpURLConnection hConn = (HttpURLConnection) url.openConnection();
				hConn.setRequestMethod("GET");
				hConn.setRequestProperty("Authorization", "Bearer " + socialAuthToken.getSoAccessAuthToken());
				hConn.setDoOutput(true);

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(hConn.getOutputStream()));
				bw.flush();
				// 결과 코드가 200이라면 성공
				int responseCode = hConn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(hConn.getInputStream()));
				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				// Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);

				System.out.println(element.getAsJsonObject());

				JsonElement detailElement = parser.parse(element.getAsJsonObject().get("kakao_account").toString());

				System.out.println(detailElement.getAsJsonObject());

				socialAuthToken.setSocialEmail(detailElement.getAsJsonObject().get("email").getAsString());

				br.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (socialType.equals("naver")) {
			try {
				URL url = new URL(naverSocialInfoUrl);

				HttpURLConnection hConn = (HttpURLConnection) url.openConnection();
				hConn.setRequestMethod("GET");
				hConn.setRequestProperty("Authorization", "Bearer " + socialAuthToken.getSoAccessAuthToken());
				hConn.setDoOutput(true);

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(hConn.getOutputStream()));
				bw.flush();
				// 결과 코드가 200이라면 성공
				int responseCode = hConn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(hConn.getInputStream()));
				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				// Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);

				System.out.println(element.getAsJsonObject());

				JsonElement detailElement = parser.parse(element.getAsJsonObject().get("response").toString());

				System.out.println(detailElement.getAsJsonObject());

				socialAuthToken.setSocialEmail(detailElement.getAsJsonObject().get("email").getAsString());

				br.close();
				bw.close();
			} catch (IOException Ie) {
				// TODO Auto-generated catch block
				System.out.println();
				Ie.printStackTrace();
			} catch (Exception e) {
				e.getStackTrace();
				System.out.println(e.getMessage());

			}

		}else if (socialType.equals("google")) {
			try {
				URL url = new URL(googleSocialInfoUrl);

				HttpURLConnection hConn = (HttpURLConnection) url.openConnection();
				hConn.setRequestMethod("GET");
				hConn.setRequestProperty("Authorization", "Bearer " + socialAuthToken.getSoAccessAuthToken());
				hConn.setDoOutput(true);

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(hConn.getOutputStream()));
				bw.flush();
				// 결과 코드가 200이라면 성공
				int responseCode = hConn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(hConn.getInputStream()));
				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				// Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);

				System.out.println(element.getAsJsonObject());

				JsonElement detailElement = parser.parse(element.getAsJsonObject().get("response").toString());

				System.out.println(detailElement.getAsJsonObject());

				socialAuthToken.setSocialEmail(detailElement.getAsJsonObject().get("email").getAsString());

				br.close();
				bw.close();
			} catch (IOException Ie) {
				// TODO Auto-generated catch block
				System.out.println();
				Ie.printStackTrace();
			} catch (Exception e) {
				e.getStackTrace();
				System.out.println(e.getMessage());

			}

		}
		return socialAuthToken;
	}

	// 소셜 로그인 이용약관 동의
	@PutMapping(value = "/auth/socialIsAgree")
	public ResponseEntity<Map<String, Object>> socilaIsAgree(@RequestBody UserInfo userInfo) {
		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		String role = "test";
		Boolean success = false;
		try {
			// 유저 No decode
			//userInfo.setUserNo(Integer.parseInt(CryptoUtil.decode(userInfo.getEnUserNo())));

			UserInfo updateUserInfo = userInfoService.detail(userInfo);
			updateUserInfo.setIsAgree(userInfo.getIsAgree());
			updateUserInfo.setUserName(userInfo.getUsername());
			updateUserInfo.setUserNickName(userInfo.getUserNickName());

			JwtAuthToken token = jwtTokenProvider.createToken(updateUserInfo.getUserId(), role);
			updateUserInfo.setUserAuthCheckToken(token.getRefreshAuthToken());
			userInfoService.updateAuthToken(updateUserInfo, updateUserInfo);

			updateUserInfo.setUserAuthCheckToken(null);
			updateUserInfo.setEnUserNo(CryptoUtil.encode(Integer.toString(updateUserInfo.getUserNo())));
			success = true;
			result.put("success", success);
			responseHeaders.set("accessAuthToken", token.getAccessAuthToken());
			responseHeaders.set("refreshAuthToken", token.getRefreshAuthToken());
			result.put("userInfo", updateUserInfo);

			return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(result);

		} catch (Exception e) {
			result.put("success", success);
			result.put("errorMsg", e.getCause().getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}
	// user 프로필
	@GetMapping(value="{enUserNo}")
	public ResponseEntity<Map<String, Object>> userProfileInfo(@RequestHeader("accessAuthToken") String accessAuthToken,
																@RequestHeader("refreshAuthToken") String refreshAuthToken,
																@PathVariable String enUserNo){
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
					UserInfo userInfo = new UserInfo();
					userInfo.setUserNo(userNo);
					userInfo = userInfoService.detail(userInfo);
					result.put("userInfo", userInfo);
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
	// 프로필 수정
	@PutMapping(value = "{enUserNo}", consumes = {"multipart/form-data"})
		public ResponseEntity<Map<String, Object>> userProfileUpdate(@RequestHeader("accessAuthToken") String accessAuthToken,
																	@RequestHeader("refreshAuthToken") String refreshAuthToken,
																	@PathVariable String enUserNo,
																	@RequestPart(value="userInfo" ,required = true) UserInfo userInfo,
																	@RequestPart(value="profileImage", required = false) MultipartFile profileImage) {
		Map<String, Object> result = new HashMap<>();
		Boolean success = false;
		int fileSaveType = 1;
		try {
			int userNo = Integer.parseInt(CryptoUtil.decode(enUserNo));
			// 권한 체크
			if(jwtTokenProvider.validateToken(accessAuthToken)) {
				success = jwtUserAuthCheck.userAuthCheck(accessAuthToken , userNo);
			}else {
				success = jwtUserAuthCheck.userAuthCheck(refreshAuthToken , userNo);
			}
			if(Boolean.TRUE.equals(success)) {
				String imageFileName = null;
				if(profileImage != null) {
					String ext = FilenameUtils.getExtension(profileImage.getOriginalFilename());
					imageFileName = enUserNo+"."+ext;
					success = fileService.saveFile(profileImage, fileSaveType, imageFileName);
				}
				 userInfo.setUserNo(userNo);
				 UserInfo updateUserInfo = userInfoService.detail(userInfo);
				 updateUserInfo.setUserName(userInfo.getUsername());
				 updateUserInfo.setUserNickName(userInfo.getUserNickName());
				 updateUserInfo.setUserPwd(CryptoUtil.getHashedPassword(userInfo.getUserPwd()));
				 updateUserInfo.setImageName(imageFileName);
				 userInfoService.update(updateUserInfo,updateUserInfo);
				 updateUserInfo.setEnUserNo(enUserNo);
				 result.put("userInfo", updateUserInfo);
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
		// 회원탈퇴
		@DeleteMapping(value = "/{enUserNo}")
		public ResponseEntity<Map<String, Object>> deleteUserInfo(@RequestHeader("accessAuthToken") String accessAuthToken,
																@RequestHeader("refreshAuthToken") String refreshAuthToken,												
																@PathVariable String enUserNo) {
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
				if(Boolean.TRUE.equals(success)) {
					// !@# 삭제 범위 
					UserInfo deleteUserInfo = new UserInfo();
					deleteUserInfo.setUserNo(userNo);
					userInfoService.delete(deleteUserInfo);
					
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
				result.put("errorMsg", e.getCause().getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
		}
		// 로그 아웃
		@PutMapping(value = "{enUserNo}/logOut")
		public ResponseEntity<Map<String, Object>> userlogOut(@RequestHeader("accessAuthToken") String accessAuthToken,
																@RequestHeader("refreshAuthToken") String refreshAuthToken,												
																@PathVariable String enUserNo) {
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
				if(Boolean.TRUE.equals(success)) {
					// !@# 삭제 범위 
					UserInfo logOutUserInfo = new UserInfo();
					logOutUserInfo.setUserNo(userNo);
					logOutUserInfo = userInfoService.detail(logOutUserInfo);
					logOutUserInfo.setUserAuthCheckToken(null);
					userInfoService.updateAuthToken(logOutUserInfo, logOutUserInfo);
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
				result.put("errorMsg", e.getCause().getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
		}

}
