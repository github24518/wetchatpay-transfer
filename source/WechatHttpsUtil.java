package com.coding.happy.go.common.utils;

import java.io.IOException;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 创建时间：2016年11月9日 下午4:16:32
 *
 * @author andy
 * @version 2.2
 */
@Slf4j
public class WechatHttpsUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";

    //链接超时时间5秒
    private static final int CONNECT_TIME_OUT = 5000;

    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
        .setConnectTimeout(CONNECT_TIME_OUT).build();

    //微信支付ssl证书
    private static SSLContext wx_ssl_context = null;

    static {
        Resource resource = new ClassPathResource("wx_apiclient_cert.p12");
        try {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            //证书密码
            char[] keyPassword = ConfigUtil.getProperty("wx.mchid").toCharArray();
            keystore.load(resource.getInputStream(), keyPassword);
            wx_ssl_context = SSLContexts.custom().loadKeyMaterial(keystore, keyPassword).build();
        } catch (Exception e) {
            log.error("加载微信支付证书失败:{}", e);
        }
    }


    /**
     * @param url 请求地址
     * @param s 参数xml
     * @return 请求失败返回null
     * @description 功能描述: post https请求，服务器双向证书验证
     */
    public static String posts(String url, String s) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = new HttpPost(url);
        String body = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom()
                .setDefaultRequestConfig(REQUEST_CONFIG)
                .setSSLSocketFactory(getSSLConnectionSocket())
                .build();
            httpPost.setEntity(new StringEntity(s, DEFAULT_CHARSET));
            response = httpClient.execute(httpPost);
            body = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        } catch (Exception e) {
            log.error("Exception:{}", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("response IOException:{}", e);
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("httpClient IOException:{}", e);
                }
            }
        }
        return body;
    }

    //获取ssl connection链接
    private static SSLConnectionSocketFactory getSSLConnectionSocket() {
        return new SSLConnectionSocketFactory(wx_ssl_context,
            new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }
}