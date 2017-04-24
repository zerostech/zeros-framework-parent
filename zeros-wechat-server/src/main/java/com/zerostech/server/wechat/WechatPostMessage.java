package com.zerostech.server.wechat;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * WechatPostMessage
 *
 * @author hzzjb
 * @date 2017/4/12
 */
public class WechatPostMessage {

    private String toUserName;

    private String fromUserName;

    private String createTime;

    private String msgType;

    private String msgId;

    private String content;

    private String picUrl;

    private String mediaId;

    private String format;

    // 还有其它的信息后面再处理

    class MessageType {
        public static final String TEXT = "text";
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void parseXml(String xml) throws Exception {
        if (xml == null || xml.equals("")) {
            throw new Exception("xml can not be null");
        }
        Document doc = DocumentHelper.parseText(xml);
        Element root = doc.getRootElement();
        this.fromUserName = getElementValue(root, "FromUserName");
        this.toUserName = getElementValue(root, "ToUserName");
        this.createTime = getElementValue(root, "CreateTime");
        this.msgType = getElementValue(root, "MsgType");
        this.msgId = getElementValue(root, "MsgId");
        if (msgType == null || msgType.equals("")) {
            throw new Exception("the msgType is error");
        }
        switch (msgType) {
            case MessageType.TEXT:
                parseText(root);
                break;
            default:
                break;
        }
    }

    private void parseText(Element element) {
        this.content = getElementValue(element, "Content");
    }

    private String getElementValue(Element element, String key) {
        try {
            return element.elementText(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
