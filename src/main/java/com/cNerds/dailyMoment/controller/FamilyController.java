package com.cNerds.dailyMoment.controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/family/")
public class FamilyController {
	@RequestMapping(method = RequestMethod.GET, path = "/{userId}") // localhost:8080/api/getRequestApi 로 들어오면 해당 getMethod api를 사용할 수 있다.
    public String getFaimilyList(@PathVariable String userId){
		
		return  userId;    
		}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{userId}/{familyName}") // localhost:8080/api/getRequestApi 로 들어오면 해당 getMethod api를 사용할 수 있다.
    public String getFaimilyInfo(@PathVariable String userId , @PathVariable String familyName){
		
		return  userId;    
		}
	
	@RequestMapping(method = RequestMethod.POST, path = "/{userId}") // localhost:8080/api/getRequestApi 로 들어오면 해당 getMethod api를 사용할 수 있다.
    public String famliyInsert(){
		return  "INSERT";    
		}

}