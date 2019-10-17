package com.qf.security.filter;

import com.qf.security.auth.UserSecurityAuth;
import com.qf.security.token.SmsAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserSecurityAuth userSecurityAuth;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken) authentication;

        String phone = (String)smsAuthenticationToken.getPrincipal();

        UserDetails userDetails = userSecurityAuth.loadUserByUsername(phone);

        SmsAuthenticationToken token = new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());

        token.setDetails(smsAuthenticationToken.getDetails());

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(SmsAuthenticationToken.class);
    }

    public UserSecurityAuth getUserSecurityAuth() {
        return userSecurityAuth;
    }

    public void setUserSecurityAuth(UserSecurityAuth userSecurityAuth) {
        this.userSecurityAuth = userSecurityAuth;
    }
}
