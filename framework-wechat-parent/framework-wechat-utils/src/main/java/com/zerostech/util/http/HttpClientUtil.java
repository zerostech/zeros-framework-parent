package com.zerostech.util.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * todo 找时间修改下
 * OkHttpClient
 * Created by hztps on 2016/9/8.
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String downLoadFromUrl(String urlStr, String fileName) throws IOException {
        String savePath = Class.class.getClass().getResource("/").getPath();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        fileName = saveDir + File.separator + fileName;
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("info:" + url + " download success");
        return fileName;
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static String sendGet(String url, String content) {
        Request.Builder build = new Request.Builder();
        String getUrl;
        if (url.endsWith("?")) {
            getUrl = url + content;
        } else {
            getUrl = url + "?" + content;
        }
        build.url(getUrl);
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("accept", "*/*");
        headerMap.put("connection", "Keep-Alive");
        headerMap.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        buildHeader(build, headerMap);
        Request request = build.build();
        OkHttpClient client = new OkHttpClient();
        return doExecute(request, client);
    }

    /**
     * post发http请求
     *
     * @param url     地址
     * @param content 发送数据体
     * @return 返回消息体
     */
    public static String sendPost(String url, String content) {
        try {
            OkHttpClient client = new OkHttpClient();
            return doPost(url, client, content);
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！", e);
        }
        return "";
    }

    public static String sendPost(String url, String content, String contentType) {
        try {
            OkHttpClient client = new OkHttpClient();
            return doPost(url, client, content, contentType);
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！", e);
        }
        return "";
    }

    /**
     * post带证书发https请求
     *
     * @param url        地址
     * @param content    发送数据体
     * @param sslContext SSL上下文
     * @return 返回消息体
     */
    public static String sendPost(String url, String content, SSLContext sslContext) {
        try {
            OkHttpClient client = new OkHttpClient();
            if (sslContext != null) {
                client.setSslSocketFactory(sslContext.getSocketFactory());
            }
            return doPost(url, client, content);
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！", e);
        }
        return "";
    }

    public static String sendPost(String url, Map<String, String> params) {
        String content = "";
        for (String name : params.keySet()) {
            if ("".equals(content)) {
                content = name + "=" + params.get(name);
            } else {
                content = content + "&" + name + "=" + params.get(name);
            }
        }
        try {
            OkHttpClient client = new OkHttpClient();
            return doPost(url, client, content, "application/x-www-form-urlencoded");
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！", e);
        }
        return "";
    }

    /**
     * 获取证书内容
     *
     * @param password     密钥密码
     * @param keyStorePath 密钥文件全路径
     * @return SSL上下文
     * @throws Exception
     */
    public static SSLContext getSSLContext(String password, String keyStorePath) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream inStream = new FileInputStream(new File(keyStorePath));
        try {
            keyStore.load(inStream, password.toCharArray());
        } finally {
            inStream.close();
        }
        return SSLContexts.custom()
                .loadKeyMaterial(keyStore, password.toCharArray())
                .build();
    }

    private static String doPost(String url, OkHttpClient client, String content, String contentType) throws Exception {
        Request.Builder build = new Request.Builder();
        build.url(url);
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("accept", "*/*");
        headerMap.put("connection", "Keep-Alive");
        headerMap.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        if (contentType != null && !contentType.isEmpty()) {
            headerMap.put("Content-Type", contentType);
        }
        buildHeader(build, headerMap);
        logger.info("OkHttp 请求body:" + content);
        RequestBody body = RequestBody.create(null, content);// contentType暂时为空，有特殊需要再改
        build.post(body);
        Request request = build.build();
        return doExecute(request, client);
    }

    /**
     * post发送请求操作
     *
     * @param url
     * @param client
     * @param content
     * @return
     * @throws Exception
     */
    private static String doPost(String url, OkHttpClient client, String content) throws Exception {
        return doPost(url, client, content, null);
    }

    private static String doExecute(Request request, OkHttpClient client) {
        String result = "";
        try {
            logger.info("OkHttp 请求url:" + request.urlString());
            Response response = client.newCall(request).execute();
            if (isSuccessful(response)) {
                result = response.body().string();
            }
        } catch (IOException e) {
            logger.error("发送 POST 请求出现异常！", e);
        } finally {
            logger.info("OkHttp 返回:" + result);
        }
        return result;
    }

    /**
     * 组装发送请求头
     *
     * @param build
     * @param headerMap
     */
    private static void buildHeader(Request.Builder build, Map<String, String> headerMap) {
        if (headerMap != null && headerMap.size() > 0) {
            Iterator<Map.Entry<String, String>> iter = headerMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                build.header(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 判断返回信息是否成功
     *
     * @param response
     * @return
     */
    private static boolean isSuccessful(Response response) {
        if (response == null) {
            return false;
        }
        return true;
//        return response.isSuccessful();
    }
}
