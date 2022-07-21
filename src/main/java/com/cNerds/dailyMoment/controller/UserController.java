package com.cNerds.dailyMoment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user/")
public class UserController {

	@RequestMapping(method = RequestMethod.GET, path = "signUp") 
    public String signUp(){
		return  "signUp";    
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
