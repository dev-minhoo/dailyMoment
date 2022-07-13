package com.cNerds.dailyMoment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RequestMapping("/api/")
public class testController {
	@RequestMapping(method = RequestMethod.GET, path = "/getRequestApi") // localhost:8080/api/getRequestApi 로 들어오면 해당 getMethod api를 사용할 수 있다.
    public String getRequestApi(){
		return "Hello Spring";    
		}

}