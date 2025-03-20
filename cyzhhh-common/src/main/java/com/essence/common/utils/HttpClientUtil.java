package com.essence.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * HttpClient GET POST请求封装工具
 *
 * @author lxf
 *
 *         2018年5月11日 上午9:18:52
 */
public class HttpClientUtil {
	private static final CloseableHttpClient httpclient;
	public static final String CHARSET = "UTF-8";

	static {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
		httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}




    public static String sendGet(CloseableHttpClient httpClient,String url) throws ClientProtocolException, IOException{
        if(httpClient==null){
            httpClient=httpclient;
        }
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String responseContent = EntityUtils.toString(httpEntity, "UTF-8");
        return responseContent;
    }

    public static String sendGetHeard(CloseableHttpClient httpClient,String url) throws ClientProtocolException, IOException{
    	HttpGet httpGet = new HttpGet(url);
//    	httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
    	// 浏览器表示
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        // 传输的类型
        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
    	CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
    	HttpEntity httpEntity = httpResponse.getEntity();
    	String responseContent = EntityUtils.toString(httpEntity, "UTF-8");
    	return responseContent;
    }



	/**
	 * HTTP Get 获取内容
	 *
	 * @param url 请求的url地址
	 *            ?之前的地址
	 * @param params 请求的参数
	 *
	 * @return 页面内容
	 */
	public static String sendGet(String url, Map<String, Object> params)
			throws ParseException, UnsupportedEncodingException, IOException {

		if (params != null && !params.isEmpty()) {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());

			for (String key : params.keySet()) {
				pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
			}
			url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), CHARSET);
		}

		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			httpGet.abort();
			throw new RuntimeException("HttpClient,error status code :" + statusCode);
		}
		HttpEntity entity = response.getEntity();
		String result = null;
		if (entity != null) {
			result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			response.close();
			System.out.println(result);
			return result;
		} else {
			return null;
		}
	}


}
