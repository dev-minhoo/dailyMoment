package com.cNerds.dailyMoment.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/")
public class testController {
	@RequestMapping(method = RequestMethod.GET, path = "/test") // localhost:8080/api/getRequestApi 로 들어오면 해당 getMethod api를 사용할 수 있다.
    public String getRequestApi(){
		return  "Hello Spring";    
		}

}