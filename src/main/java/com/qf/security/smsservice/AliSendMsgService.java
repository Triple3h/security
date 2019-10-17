package com.qf.security.smsservice;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class AliSendMsgService extends AbstractSendMsgService {

    private String sign;

    @Override
    public Object sendMsg() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.getAccessKeyId(), this.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", this.getMobile());  //手机号
        request.putQueryParameter("SignName", this.getSign()); //签名保证数据的安装传输
        request.putQueryParameter("TemplateCode", this.getTemplateId());  //模板id

        // 因为在模板中定义了  $code
        request.putQueryParameter("TemplateParam", "{code:" + this.getCode() + "}"); //

        try {
            return client.getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AliSendMsgService(String accessKeyId, String accessKeySecret, String templateId, String code, String mobile, String sign) {
        super(accessKeyId, accessKeySecret, templateId, code, mobile);
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
