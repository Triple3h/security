package com.qf.security.filter;

import com.qf.security.token.SmsAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_PHONE = "phone";

    private String phoneParameter = SPRING_SECURITY_FORM_PHONE;

    private boolean postOnly = true;

    public SmsAuthenticationFilter() {
        super(new AntPathRequestMatcher("/authentication/sms", "POST"));
    }


    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String phone = obtainPhone(request);

        if (phone == null) {
            phone = "";
        }


        phone = phone.trim();

        SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken(phone);

        setDetails(request, smsAuthenticationToken);

        return this.getAuthenticationManager().authenticate(smsAuthenticationToken);
    }

    //========================================================================================================

    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(phoneParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setPhoneParameter(String usernameParameter) {
        Assert.hasText(phoneParameter, "Phone parameter must not be empty or null");
        this.phoneParameter = usernameParameter;
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getPhoneParameter() {
        return phoneParameter;
    }

}
