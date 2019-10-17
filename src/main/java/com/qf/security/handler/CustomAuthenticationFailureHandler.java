package com.qf.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.qf.security.exception.CodeValidateException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 81958
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        Map<String, Object> map = new HashMap<>();

        // 用户名或者秘密错误，在UserDetailsService中，将异常已经抛过来了
        if(exception instanceof AuthenticationServiceException) {
            map.put("code", -2);
            map.put("msg", exception.getMessage());
        }else if(exception instanceof CodeValidateException){
            map.put("code", -5);
            map.put("msg", exception.getMessage());
        } else if(exception instanceof BadCredentialsException) {
            map.put("code", -1);
            map.put("msg", exception.getMessage());
        }else if(exception instanceof UsernameNotFoundException) {
            map.put("code", -2);
            map.put("msg", exception.getMessage());
        }

        response.setContentType("application/json;charset=utf-8");

        response.getWriter().write(JSONObject.toJSONString(map));
    }
}
