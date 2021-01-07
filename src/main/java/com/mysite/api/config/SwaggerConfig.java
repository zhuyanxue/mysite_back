package com.mysite.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;


@Configuration

//开启swagger3
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo())
                .groupName("青彦沐")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mysite.api.controller"))
                .build();
    }

    /*
    页面信息
     */
    private ApiInfo apiInfo(){
        Contact contact=new Contact("青彦沐","https://space.bilibili.com/100036156","1789204734@qq.com");
        return new ApiInfo(
                "mysite后端接口文档" ,
                "静以修身，俭以养德。",
                "v1.0",
                "https://space.bilibili.com/100036156",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }
}
