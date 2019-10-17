package com.qf.security.smsservice;

public abstract class AbstractSendMsgService {

    //运营商提供的key
    private String accessKeyId;

    // 运营商提供的secret
    private String accessKeySecret;

    //模板ID
    private String templateId;

    //随机验证码
    private String code;

    //手机号
    private String mobile;

    public abstract Object sendMsg();

    public AbstractSendMsgService(String accessKeyId, String accessKeySecret, String templateId, String code, String mobile) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.templateId = templateId;
        this.code = code;
        this.mobile = mobile;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
