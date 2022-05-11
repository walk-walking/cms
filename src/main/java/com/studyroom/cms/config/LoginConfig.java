package com.studyroom.cms.config;

import com.studyroom.cms.interceptor.AdminLoginInterceptor;
import com.studyroom.cms.interceptor.StudentLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //未添加进去的接口则不拦截
        //2个拦截器 按照顺序依次拦截
        registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/student/**")
                .excludePathPatterns("/student/modpwd")
                .excludePathPatterns("/student/modemail")

                .addPathPatterns("/studyroom/**")
                .addPathPatterns("/studyseat/**");

        registry.addInterceptor(new StudentLoginInterceptor())
                .addPathPatterns("/reserve/**")
                .addPathPatterns("/student/modpwd")
                .addPathPatterns("/student/modemail");

    }


}
