package com.example.config;

import com.example.security.ratelimit.ApiBucketManager;
import com.example.security.ratelimit.ApiLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WebConfig implements WebMvcConfigurer {

    private final ApiBucketManager apiBucketManager;

    @Value("${spring.api-limit.enable}")
    private boolean enabledApiLimit;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api",
                HandlerTypePredicate.forAnnotation(RestController.class));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (enabledApiLimit) {
            ApiLimitInterceptor commonApiLimitIntercept = new ApiLimitInterceptor(apiBucketManager, 100, Duration.ofMinutes(1));
            registry.addInterceptor(commonApiLimitIntercept)
                    .addPathPatterns("/api/**")
                    .excludePathPatterns("/api/upload", "/api/auth/**");

            ApiLimitInterceptor uploadApiLimitIntercept = new ApiLimitInterceptor(apiBucketManager, 3, Duration.ofSeconds(10), "api-upload");
            registry.addInterceptor(uploadApiLimitIntercept)
                    .addPathPatterns("/api/upload");

            ApiLimitInterceptor authApiLimitIntercept = new ApiLimitInterceptor(apiBucketManager, 3, Duration.ofSeconds(5), "api-auth");
            registry.addInterceptor(authApiLimitIntercept)
                    .addPathPatterns("/api/auth/**");
        }
    }

}