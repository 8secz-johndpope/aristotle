package com.aristotle.core.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HttpUtil {

    private HttpClientBuilder httpClientBuilder;
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
	
	public HttpUtil(){
		poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        poolingHttpClientConnectionManager.setMaxTotal(5);
        // Increase default max connection per route to 20
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(5);

        httpClientBuilder = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager);
	}

    private HttpClient getHttpClient() {
        return httpClientBuilder.build();
    }

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private void printPoolStats(String prefix){
		PoolStats poolStats = poolingHttpClientConnectionManager.getTotalStats();
		System.out.println(prefix +":" +poolStats.toString());
	}
    public String getResponse(String url) throws ClientProtocolException, IOException {
		logger.info("Hitting Url = {}", url);
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpClient = (CloseableHttpClient)HttpClients.custom().setConnectionManager
			    (poolingHttpClientConnectionManager).build();;
		printPoolStats("During Http Call");
        HttpResponse httpResponse = httpClient.execute(httpGet);
		//System.out.println("Got Response= "+ httpResponse);
		HttpEntity httpEntity = httpResponse.getEntity();
		//System.out.println("Converting to String= "+ httpEntity);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//System.out.println("IOUtils.copy(httpEntity.getContent(), byteArrayOutputStream);");
		IOUtils.copy(httpEntity.getContent(), byteArrayOutputStream);
		String response = byteArrayOutputStream.toString();
		logger.info("response = {}", response);
		try{
			EntityUtils.consume(httpEntity);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		printPoolStats("After Http Call");
		return response;
	}

    public String getResponse(String url, String apiKey, String secret) throws ClientProtocolException, IOException {
        logger.info("Hitting Url = {}", url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("api_key", apiKey);
        httpGet.setHeader("auth_token", secret);
        HttpResponse httpResponse = getHttpClient().execute(httpGet);
        // System.out.println("Got Response= "+ httpResponse);
        HttpEntity httpEntity = httpResponse.getEntity();
        // System.out.println("Converting to String= "+ httpEntity);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // System.out.println("IOUtils.copy(httpEntity.getContent(), byteArrayOutputStream);");
        IOUtils.copy(httpEntity.getContent(), byteArrayOutputStream);
        String dayDonationString = byteArrayOutputStream.toString();
        return dayDonationString;
    }
	
	public  String postSoapRequest(String url, String payLoad) throws ClientProtocolException, IOException{
		logger.info("Hitting Url = {}", url);
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = new StringEntity(payLoad);
		httpPost.setEntity(entity);
		httpPost.addHeader("Content-Type", "application/soap+xml");
		httpPost.addHeader("charset", "utf-8");
        HttpResponse httpResponse = getHttpClient().execute(httpPost);
		//System.out.println("Got Response= "+ httpResponse);
		HttpEntity httpEntity = httpResponse.getEntity();
		//System.out.println("Converting to String= "+ httpEntity);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//System.out.println("IOUtils.copy(httpEntity.getContent(), byteArrayOutputStream);");
		IOUtils.copy(httpEntity.getContent(), byteArrayOutputStream);
		String responseString = byteArrayOutputStream.toString();
		System.out.println("responseString="+responseString);
		return responseString;
	}
	
    public void saveHttpImage(String url, String filePath) throws ClientProtocolException, IOException {
		logger.info("Hitting Url = {}", url);
		logger.info("ans saving to  = {}", filePath);
		HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = getHttpClient().execute(httpGet);
		//System.out.println("Got Response= "+ httpResponse);
		HttpEntity httpEntity = httpResponse.getEntity();
		//System.out.println("Converting to String= "+ httpEntity);
		FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
		//System.out.println("IOUtils.copy(httpEntity.getContent(), byteArrayOutputStream);");
		IOUtils.copy(httpEntity.getContent(), fileOutputStream);
		fileOutputStream.close();
	}
}
