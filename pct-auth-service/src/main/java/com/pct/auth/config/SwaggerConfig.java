package com.pct.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket postsApi() {

		return new Docket(DocumentationType.SWAGGER_2).groupName("public-api").apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.pct"))
				.paths(PathSelectors.any()).build().securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.newArrayList(apiKey()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("PCT Auth APIs").description("PCT Auth API reference for developers")
				.termsOfServiceUrl("http://pct.com").license("PCT License").licenseUrl("pct@gmail.com")
				.version("1.0").build();
	}

	@SuppressWarnings("deprecation")
	@Bean
	springfox.documentation.swagger.web.SecurityConfiguration security() {
		return new springfox.documentation.swagger.web.SecurityConfiguration(null, null, null, null, "",
				ApiKeyVehicle.HEADER, "AUTHORIZATION", null);
	}

	private ApiKey apiKey() {
		return new ApiKey("AUTHORIZATION", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/api.*"))
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference("AUTHORIZATION", authorizationScopes));
	}

}
