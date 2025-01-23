package com.en.athena.config;

import com.en.athena.filters.RateLimiterFilter;
import com.en.athena.filters.TrafficLogFilter;
import com.en.athena.services.RateLimiterService;
import com.en.athena.services.TrafficLogService;
import com.en.athena.services.UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TrafficLogFilter> trafficLogFilter(TrafficLogService trafficLogService) {
        FilterRegistrationBean<TrafficLogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TrafficLogFilter(trafficLogService));
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilter(
            RateLimiterService rateLimiterService,
            UserService userService) {

        FilterRegistrationBean<RateLimiterFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RateLimiterFilter(rateLimiterService,userService));

        registrationBean.setOrder(2);

        return registrationBean;
    }
}

