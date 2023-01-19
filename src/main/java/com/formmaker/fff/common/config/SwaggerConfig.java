package com.formmaker.fff.common.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.formmaker.fff"))
                .paths(PathSelectors.any())
                .build()
                // jwt 토큰 전역변수 설정
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()));
    }

    private ApiKey apiKey() {
        return new ApiKey(SECURITY_SCHEMA_NAME, "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    public static final String SECURITY_SCHEMA_NAME = "JWT";
    public static final String AUTHORIZATION_SCOPE_GLOBAL = "global";
    public static final String AUTHORIZATION_SCOPE_GLOBAL_DESC = "accessEverything";

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(AUTHORIZATION_SCOPE_GLOBAL, AUTHORIZATION_SCOPE_GLOBAL_DESC);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(SECURITY_SCHEMA_NAME, authorizationScopes));
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .build();
    }

    // Api 정보 담아둔 메서드 생성
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Rest API Test")
                .description("Board API")
                .version("1.0")
                .build();
    }
}