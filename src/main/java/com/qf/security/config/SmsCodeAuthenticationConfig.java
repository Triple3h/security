package com.qf.security.config;

import com.qf.security.auth.UserSecurityAuth;
import com.qf.security.filter.SmsAuthenticationFilter;
import com.qf.security.filter.SmsAuthenticationProvider;
import com.qf.security.handler.CustomAuthenticationFailureHandler;
import com.qf.security.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SmsCodeAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private UserSecurityAuth userSecurityAuth;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        SmsAuthenticationFilter filter = new SmsAuthenticationFilter();
        // 获取AuthenticationManager => filter和provider之间的桥梁
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        // 设置成功或失败处理方式
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        SmsAuthenticationProvider provider = new SmsAuthenticationProvider();
        // 因为在 SmsAuthenticationProvider要根据电话查询用户信息
        provider.setUserSecurityAuth(userSecurityAuth);

        // 统一设置Filter和Provider
        http.authenticationProvider(provider).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
