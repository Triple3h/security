package com.qf.security.controller;

import com.qf.security.code.ValidateCode;
import com.qf.security.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sms/code")
@Slf4j
public class SmsController {

    @Resource
    private SmsService smsService;

    // Session的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    public static final String SMS_KEY_CODE = "SMS_KEY_CODE";

    @RequestMapping
    public void sendMsg(HttpServletRequest request, String phone) {

        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange(new char['0']['9']).build();

        ValidateCode validateCode = new ValidateCode(randomStringGenerator.generate(6), 120);

        sessionStrategy.setAttribute(new ServletWebRequest(request), SMS_KEY_CODE, validateCode);

        log.info(validateCode.getcode());

        smsService.sendMsg(validateCode.getcode(), phone);
    }
}
