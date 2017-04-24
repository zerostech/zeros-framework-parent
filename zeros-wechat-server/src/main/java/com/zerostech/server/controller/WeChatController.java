package com.zerostech.server.controller;

import java.security.DigestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.zerostech.server.wechat.WechatGetMessage;
import com.zerostech.server.wechat.WechatPostMessage;
import com.zerostech.util.wechat.AccessTokenUtil;

/**
 * WechatController
 *
 * @author hzzjb
 * @date 2017/4/7
 */
@RestController
@RequestMapping("we/chat")
public class WeChatController {

    private final String TOKEN = "0612E9B2FF5E44C1B4BB310F237484E3";

    private Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @RequestMapping("access/token")
    @ResponseBody
    public String getAccessToken() {
        logger.info("getAccessToken begin");
        try {
            return AccessTokenUtil.getAccessToken();
        }finally {
            logger.info("getAccessToken end");
        }
    }

    @RequestMapping("test")
    @ResponseBody
    public String test(@RequestBody String str) {
        logger.info(str);
        return "";
    }

    @RequestMapping(value = "handle", method = RequestMethod.GET)
    @ResponseBody
    public String handleMessage(WechatGetMessage model) {
        logger.info(JSON.toJSONString(model));
        try {
            if (model.checkSignature(TOKEN)) {
                return model.getEchostr();
            }
        } catch (DigestException e) {
            return "";
        }
        return model.getEchostr();
    }

    @RequestMapping(value = "handle", method = RequestMethod.POST)
    @ResponseBody
    public String handleMessage(@RequestBody String messageBody) {
        logger.info(messageBody);
        WechatPostMessage message = new WechatPostMessage();
        try {
            message.parseXml(messageBody);
            logger.info(JSON.toJSONString(message));
        } catch (Exception e) {
            logger.error("解析消息失败", e);
        }
        return "success";
    }

}
