package com.qf.security.config;

import com.qf.security.auth.UserSecurityAuth;
import com.qf.security.filter.ValidateImageCodeFilter;
import com.qf.security.filter.ValidateSmsCodeFilter;
import com.qf.security.handler.CustomAuthenticationFailureHandler;
import com.qf.security.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author 81958
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 该bean的作用是，在UserDetailsService接口的loadUserByUsername返回的UserDetail中包含了
     * password, 该bean就将用户从页面提交过来的密码进行处理，处理之后与UserDetail中密码进行比较。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserSecurityAuth userSecurityAuth;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private ValidateImageCodeFilter validateImageCodeFilter;

    @Autowired
    private SmsCodeAuthenticationConfig smsCodeAuthenticationConfig;

    @Autowired
    private ValidateSmsCodeFilter validateSmsCodeFilter;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();

        jdbcTokenRepository.setDataSource(dataSource);

        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //将图片验证码过滤器，加载用户名密码验证过滤器之前
        http.addFilterBefore(validateImageCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(validateSmsCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                //表单登录
                .loginPage("/login.html") //指定登录页面
                .loginProcessingUrl("/authentication/form") //form表单提交到哪儿
                .successHandler(customAuthenticationSuccessHandler) //成功处理
                .failureHandler(customAuthenticationFailureHandler) //失败处理
                //记住我功能
                .and() //
                .rememberMe() //开启记住我
                .tokenValiditySeconds(36000000) //记住多久
                .tokenRepository(persistentTokenRepository()) //上面定义的bean
                .userDetailsService(userSecurityAuth) //使用记住我需要再次验证
                //拦截请求设置
                .and() //
                .authorizeRequests() //所有都需要认证
                .antMatchers("/login.html", "/js/**", "/css/**","/validate/imageCode","/sms/code") //过滤掉哪些不需要
                .permitAll() //上述的都放过去
                .anyRequest() //拦截所有
                .authenticated() //
                
                .and() //
                .csrf().disable() //关闭跨站请求伪造功能
                .apply(smsCodeAuthenticationConfig);
    }

}
