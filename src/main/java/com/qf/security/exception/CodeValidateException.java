package com.qf.security.exception;

import org.springframework.security.core.AuthenticationException;


/**
 * @author 81958
 */
public class CodeValidateException extends AuthenticationException {

    public CodeValidateException(String msg) {
        super(msg);
    }

}
