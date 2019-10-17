package com.qf.security.filter;

import com.qf.security.code.ImageCode;
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

/**
 * @author 81958
 */
@Component
public class ValidateImageCodeFilter extends OncePerRequestFilter {

    public static final String IMG_CODE = "validateCode";

    public static final String POST_METHOD = "POST";

    public static final String PWD_LOGIN = "/authentication/form";

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (POST_METHOD.equals(request.getMethod()) && PWD_LOGIN.equals(request.getRequestURI())) {

            try {
                validateImgCode(new ServletWebRequest(request));
            } catch (AuthenticationException ex) {
                customAuthenticationFailureHandler.onAuthenticationFailure(request, response, ex);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    protected void validateImgCode(ServletWebRequest webRequest) throws CodeValidateException, ServletRequestBindingException {

        ImageCode imageCode = (ImageCode) sessionStrategy.getAttribute(webRequest, ImageCodeController.VALIDATE_IMG_CODE);

        String code = ServletRequestUtils.getStringParameter(webRequest.getRequest(), IMG_CODE);

        if (StringUtils.isEmpty(code)) {
            throw new CodeValidateException("验证码不能为空");
        }

        if (imageCode == null) {
            throw new CodeValidateException("验证码不存在");
        }

        if (imageCode.isExpired()) {
            sessionStrategy.removeAttribute(webRequest, ImageCodeController.VALIDATE_IMG_CODE);
            throw new CodeValidateException("验证码过期");
        }

        if (!code.equals(imageCode.getcode())) {
            throw new CodeValidateException("验证码不正确");
        }

        sessionStrategy.removeAttribute(webRequest, ImageCodeController.VALIDATE_IMG_CODE);
    }
}
