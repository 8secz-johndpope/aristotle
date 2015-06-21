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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HttpUtil {

    private HttpClientBuilder httpClientBuilder;
	
	public HttpUtil(){
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
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
	
	public static void main(String[] args) throws ClientProtocolException, IOException{
		String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">" +
				"<soap12:Body>" +
				"<DonorStatus xmlns=\"http://tempuri.org/\">" +
				"<DonorTransactionID>NI338217</DonorTransactionID>" +
				"<DonorEmailId></DonorEmailId>" +
				"</DonorStatus>" +
				"</soap12:Body>" +
				"</soap12:Envelope>";
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.postSoapRequest("https://donate.aamaadmiparty.org/webservice/donationservice.asmx", request);
	}
	
    public String getResponse(String url) throws ClientProtocolException, IOException {
		logger.info("Hitting Url = {}", url);
		HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = getHttpClient().execute(httpGet);
		//System.out.println("Got Response= "+ httpResponse);
		HttpEntity httpEntity = httpResponse.getEntity();
		//System.out.println("Converting to String= "+ httpEntity);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//System.out.println("IOUtils.copy(httpEntity.getContent(), byteArrayOutputStream);");
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
