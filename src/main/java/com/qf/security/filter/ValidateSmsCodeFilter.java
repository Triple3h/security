package com.qf.security.filter;

import com.qf.security.code.ImageCode;
import com.qf.security.code.ValidateCode;
import com.qf.security.controller.ImageCodeController;
import com.qf.security.exception.CodeValidateException;
import com.qf.security.handler.CustomAuthenticationFailureHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.qf.security.controller.SmsController.SMS_KEY_CODE;
import static com.qf.security.filter.ValidateImageCodeFilter.POST_METHOD;

@Component
public class ValidateSmsCodeFilter extends OncePerRequestFilter {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public static final String SMS_LOGIN = "/authentication/sms";
    public static final String SMS_CODE = "smsCode";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (POST_METHOD.equals(request.getMethod()) && SMS_LOGIN.equals(request.getRequestURI())) {

            try {
                validateSmsCode(new ServletWebRequest(request));
            } catch (AuthenticationException ex) {
                customAuthenticationFailureHandler.onAuthenticationFailure(request, response, ex);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    protected void validateSmsCode(ServletWebRequest webRequest) throws CodeValidateException, ServletRequestBindingException {

        ValidateCode validateCode = (ValidateCode) sessionStrategy.getAttribute(webRequest, SMS_KEY_CODE);

        String code = ServletRequestUtils.getStringParameter(webRequest.getRequest(), SMS_CODE);

        if (StringUtils.isEmpty(code)) {
            throw new CodeValidateException("验证码不能为空");
        }

        if (validateCode == null) {
            throw new CodeValidateException("验证码错误");
        }

        if (validateCode.isExpired()) {
            sessionStrategy.removeAttribute(webRequest, SMS_KEY_CODE);
            throw new CodeValidateException("验证码过期");
        }

        if (!code.equals(validateCode.getcode())) {
            throw new CodeValidateException("验证码不正确");
        }

        sessionStrategy.removeAttribute(webRequest, SMS_KEY_CODE);
    }
}
