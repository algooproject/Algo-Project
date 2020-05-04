package com.algotrading.tickerservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TickerServiceClient {
	public static final String URL = "http://localhost:8081/rsi/";
	// one instance, reuse
//	private final CloseableHttpClient httpClient = HttpClients.createDefault();
//
//	private final static ObjectMapper objectMapper = new ObjectMapper();

	private HttpClient httpClient = new HttpClient();
	private JsonMapper jsonMapper = new JsonMapper();

	public Map<String, Double> getAllRSIEntries(String code) {
		try {
			String jsonString = httpClient.sendGet(URL + "findRSIEntryMapByCode/" + code);
			return JsonMapper.toPrimitiveMap(jsonString, Double.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<RSIEntry> findRSIEntryByCode(String code) {
		try {
			String jsonString = httpClient.sendGet(URL + "findRSIEntryByCode/" + code);
			return JsonMapper.toList(jsonString, RSIEntry.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Ticker> findTickerByCode(String code) {
		try {
			String jsonString = httpClient.sendGet(URL + "findTickerByCode/" + code);
			return JsonMapper.toList(jsonString, Ticker.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		TickerServiceClient obj = new TickerServiceClient();
		System.out.println(obj.getAllRSIEntries("2800.HK,2821.HK"));
		System.out.println(obj.findRSIEntryByCode("2800.HK"));
		System.out.println(obj.findTickerByCode("2800.HK"));
	}
}
