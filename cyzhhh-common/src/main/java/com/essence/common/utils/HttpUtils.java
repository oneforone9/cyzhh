package com.essence.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.springframework.util.StringUtils.*;

/**
 * @author zhy
 * @since 2022/6/8 11:32
 */
@Slf4j
public class HttpUtils {

    public static RequestConfig setRequestConfig(int connectTimeout, int connectionRequestTimeout, int socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(connectTimeout)
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(socketTimeout)
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(true).build();
        return requestConfig;
    }

    /**
     * get请求
     *
     * @param path 请求地址
     * @return
     * @throws Exception
     */
    public static String doGet(String path)
            throws Exception {
        return doGet(path, null, null);
    }

    /**
     * get请求
     *
     * @param path          请求地址
     * @param requestConfig 请求配置
     * @return
     * @throws Exception
     */
    public static String doGet(String path, RequestConfig requestConfig)
            throws Exception {
        return doGet(path, null, requestConfig);
    }

    /**
     * get请求
     *
     * @param path    请求地址
     * @param headers 请求头
     * @return
     * @throws Exception
     */
    public static String doGet(String path, Map<String, String> headers)
            throws Exception {
        return doGet(path, headers, null);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param)
    {
        return sendGet(url, param, "UTF-8");
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param contentType 编码类型
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String contentType)
    {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try
        {
            String urlNameString = isNotBlank(param) ? url + "?" + param : url;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            log.info("sendPost - {}", url);
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }


    /**
     * get请求
     *
     * @param path          请求地址
     * @param headers       请求头
     * @param requestConfig 请求配置
     * @return
     * @throws Exception
     */
    public static String doGet(String path, Map<String, String> headers, RequestConfig requestConfig)
            throws Exception {

        HttpGet request = new HttpGet(path);
        // 请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 配置信息
        if (null != requestConfig) {
            request.setConfig(requestConfig);
        }

        return execute(request);
    }

    /**
     * post请求
     *
     * @param path 请求地址
     * @param body 请求参数
     * @return
     * @throws Exception
     */
    public static String doPost(String path, String body)
            throws Exception {

        return doPost(path, body, null, null);
    }

    /**
     * post请求
     *
     * @param path    请求地址
     * @param body    请求参数
     * @param headers 请求头
     * @return
     * @throws Exception
     */
    public static String doPost(String path, String body, Map<String, String> headers)
            throws Exception {

        return doPost(path, body, headers, null);
    }

    /**
     * post请求
     *
     * @param path          请求地址
     * @param body          请求参数
     * @param requestConfig 请求配置
     * @return
     * @throws Exception
     */
    public static String doPost(String path, String body, RequestConfig requestConfig)
            throws Exception {

        return doPost(path, body, null, requestConfig);
    }

    /**
     * post请求
     *
     * @param path          请求地址
     * @param body          请求参数
     * @param headers       请求头
     * @param requestConfig 请求配置
     * @return
     * @throws Exception
     */
    public static String doPost(String path, String body, Map<String, String> headers, RequestConfig requestConfig)
            throws Exception {
        // 设置post
        HttpPost request = new HttpPost(path);
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        request.setHeader("Content-Type", "application/json;charset=utf8");
        // 请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 请求体
        if (!isEmpty(body)) {
            StringEntity entity = new StringEntity(body, "UTF-8");
            request.setEntity(entity);

        }
        // 配置信息
        if (null != requestConfig) {
            request.setConfig(requestConfig);
        }

        return execute(request);
    }

    /**
     * 请求
     *
     * @param request 请求
     * @return
     * @throws Exception
     */
    public static String execute(HttpUriRequest request)
            throws Exception {
        // 创建连接
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 发起请求
        HttpResponse response = httpClient.execute(request);
        HttpEntity responseEntity = response.getEntity();
        // 获取结果
        String result = EntityUtils.toString(responseEntity);

        // 释放资源
        if (httpClient != null) {
            httpClient.close();
        }
        return result;
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }

                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
