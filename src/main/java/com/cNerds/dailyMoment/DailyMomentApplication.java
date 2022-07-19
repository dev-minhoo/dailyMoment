package com.cNerds.dailyMoment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//http://localhost:8080/swagger-ui.html

@SpringBootApplication

public class DailyMomentApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyMomentApplication.class, args);
	}
//	@Bean
//    public Docket swaggerApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                    .apis(RequestHandlerSelectors.basePackage("com.cNerds.dailyMoment.controller"))
//                    .paths(PathSelectors.any())
//                .build()
//                .apiInfo(new ApiInfoBuilder().version("1.0").title("Organization API").description("Documentation Organization API v1.0").build());
//    }
	
}
