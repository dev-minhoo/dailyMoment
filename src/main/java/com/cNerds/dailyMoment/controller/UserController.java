package com.cNerds.dailyMoment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.user.UserInfo;
import com.cNerds.dailyMoment.user.service.UserInfoService;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private UserInfoService userInfoService;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, path = "signUp") 
    public JsonObject createMember(@RequestBody UserInfo postMemberReq) {
		JsonObject result = new JsonObject();
		Boolean success = false;
		try {
			postMemberReq.setUserPwd(CryptoUtil.getHashedPassword(postMemberReq.getUserPwd()));
			userInfoService.insert(postMemberReq, postMemberReq);
			
			success = true;
			result.addProperty("success", success);
	    } catch (Exception e) {
	    	result.addProperty("success", success);
	    }

		
		
		return result;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "login") 
    public ModelAndView loginForm(){
		ModelAndView mav = new ModelAndView();
        mav.setViewName("loginForm");
        mav.addObject("모델명", "hihi"); 
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "loginAuth") 
    public String loginAuth(){
		return  "loginAuth";    
		}
	
	
	@RequestMapping(method = RequestMethod.GET, path = "kakaoLogin") 
    public String kakaoLogin(){
		return  "kakaoLogin";    
		}
	
	@RequestMapping(method = RequestMethod.GET, path = "googleLogin") 
    public String googleLogin(){
		return  "googleLogin";    
		}
	
	@RequestMapping(method = RequestMethod.GET, path = "naverLogin") 
    public String naverLogin(){
		return  "naverLogin";    
		}
	
	
}
