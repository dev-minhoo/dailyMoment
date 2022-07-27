package com.cNerds.dailyMoment.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//http://localhost:8080/swagger-ui.html
@ConditionalOnExpression(value = "${swagger.enable:true}")
@Configuration
@EnableSwagger2
public class Swagger2Config {
    
    
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DAILY_MOMENT_API")
                .description("CNERDS_FIRST_API")
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("dailyMoment")
                .apiInfo(this.apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.cNerds.dailyMoment.controller"))//controller package name
                .paths(PathSelectors.any())
                .build();
    }
}