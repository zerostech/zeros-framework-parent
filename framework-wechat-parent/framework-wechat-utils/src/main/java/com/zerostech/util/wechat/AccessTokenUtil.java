package com.zerostech.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zerostech.util.http.HttpClientUtil;

/**
 * AccessTokenUtil
 *
 * @author hzzjb
 * @date 2017/4/7
 */
public class AccessTokenUtil {
    private static String accessToken;
    private static final String APP_ID = "wx2bf27f5acf122a3b";
    private static final String APP_SECRET = "6704917244e0b821a13ce212de5a4950";
    private static final String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?";
    private static final String access_token_content = "grant_type=client_credential&appid=" + APP_ID + "&secret=" + APP_SECRET;

    public static String getAccessToken() {
        if (accessToken == null || accessToken.isEmpty()) {
            refreshToken();
        }
        return accessToken;
    }

    public static void refreshToken() {
        String url = access_token_url;
        String content = access_token_content;
        String str = HttpClientUtil.sendGet(url, content);
        JSONObject json = JSON.parseObject(str);
        String token = json.getString("access_token");
        accessToken = token;
    }

    public static void main(String[] args) {
        String url = access_token_url;
        String content = access_token_content;
        String str = HttpClientUtil.sendGet(url, content);
        System.out.println(str);
    }
}
