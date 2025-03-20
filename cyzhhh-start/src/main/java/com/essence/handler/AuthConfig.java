package com.essence.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @ClassName AuthConfig
 * @Description 认证拦截器
 * @Author zhichao.xing
 * @Date 2019/7/31 14:47
 * @Version 1.0
 **/
@Configuration
public class AuthConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthInterceptor authInterceptor;
    @Value("${auth.config.patterns}")
    private List<String> patterns;
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**")
                .excludePathPatterns(patterns);

    }
}
