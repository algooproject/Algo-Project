package com.algotrading.tickerservice;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    private void close() throws IOException {
        httpClient.close();
    }

    public String sendGet(String url) throws Exception {
        return sendGet(url, new HashMap<>());
    }

    public String sendGet(String url, Map<String, String> param) throws Exception {

        HttpGet request = new HttpGet(url);

//		 add request headers
//        request.addHeader("custom-key", "mkyong");
//        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
        for (Map.Entry<String, String> entry : param.entrySet())
        {
            request.addHeader(entry.getKey(), entry.getValue());
        }

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
//            System.out.println(response.getStatusLine()
//                    .toString());

            HttpEntity entity = response.getEntity();
//            Header headers = entity.getContentType();
//            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                return EntityUtils.toString(entity);
            }

        }
        return null;

    }

    private String sendPost(String url) throws Exception {
        return sendPost(url, new HashMap<>());
    }

    private String sendPost(String url, Map<String, String> param) throws Exception {

        HttpPost post = new HttpPost(url);

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", "abc"));
        urlParameters.add(new BasicNameValuePair("password", "123"));
        urlParameters.add(new BasicNameValuePair("custom", "secret"));

        for (Map.Entry<String, String> entry : param.entrySet())
        {
            urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

//            System.out.println(EntityUtils.toString(response.getEntity()));
            return EntityUtils.toString(response.getEntity());
        }

    }
}
