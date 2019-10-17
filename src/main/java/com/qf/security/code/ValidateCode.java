package com.qf.security.code;

import java.time.LocalDateTime;

/**
 * @author 81958
 */
public class ValidateCode {

    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, int seconds) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(seconds);
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }

    public ValidateCode() {
    }

    public String getcode() {
        return code;
    }

    public void setcode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
