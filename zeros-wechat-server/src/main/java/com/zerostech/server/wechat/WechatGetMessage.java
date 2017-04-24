package com.zerostech.server.wechat;

import java.security.DigestException;
import java.util.ArrayList;
import java.util.List;

import com.zerostech.util.sign.SignUtil;

/**
 * WechatMessageModel
 *
 * @author hzzjb
 * @date 2017/4/12
 */
public class WechatGetMessage {

    // 微信加密签名
    private String signature;

    // 	时间戳
    private String timestamp;

    // 随机数
    private String nonce;

    // 随机字符串
    private String echostr;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEchostr() {
        return echostr;
    }

    public void setEchostr(String echostr) {
        this.echostr = echostr;
    }

    public boolean checkSignature(String token) throws DigestException {
        if (token == null || token.isEmpty()) {
            return false;
        }
        List<String> list = new ArrayList<>();
        appendList(list, token);
        appendList(list, getNonce());
        appendList(list, getTimestamp());
        String sign = SignUtil.SHA1(list);
        return  (sign.equalsIgnoreCase(getSignature()));
    }

    private void appendList(List<String> list,String value) {
        if (value != null && value.isEmpty()) {
            list.add(value);
        }
    }
}
