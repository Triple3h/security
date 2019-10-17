package com.qf.security.service;

import com.aliyuncs.CommonResponse;
import com.qf.security.smsservice.AbstractSendMsgService;
import com.qf.security.smsservice.AliSendMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.templateId}")
    private String templateId;

    @Value("${aliyun.sms.sign}")
    private String sign;

    public void sendMsg(String code, String phone) {

        AbstractSendMsgService abstractSendMsgService
                = new AliSendMsgService(accessKeyId, accessKeySecret, templateId, code, phone, sign);

        CommonResponse commonResponse = (CommonResponse) abstractSendMsgService.sendMsg();


        /*
           需要入库
         */
        log.info(commonResponse.getData());
    }
}
