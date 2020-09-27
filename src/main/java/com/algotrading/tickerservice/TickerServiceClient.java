package com.algotrading.tickerservice;

import java.util.List;
import java.util.Map;

public class TickerServiceClient {
    public static final String URL = "http://localhost:8081";
    // one instance, reuse
//	private final CloseableHttpClient httpClient = HttpClients.createDefault();
//
//	private final static ObjectMapper objectMapper = new ObjectMapper();

    private HttpClient httpClient = new HttpClient();
    private JsonMapper jsonMapper = new JsonMapper();

    public Map<String, Double> getAllRSIEntries(String code) {
        try {
            String jsonString = httpClient.sendGet(URL + "/rsi/findrsientrymapbycode/" + code);
            return JsonMapper.toPrimitiveMap(jsonString, Double.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RSIEntry> findRSIEntryByCode(String code) {
        try {
            String jsonString = httpClient.sendGet(URL + "/rsi/findrsientrybycode/" + code);
            return JsonMapper.toList(jsonString, RSIEntry.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Ticker> findTickerByCode(String code) {
        try {
            String jsonString = httpClient.sendGet(URL + "/ticker/findtickerbycode/" + code);
            return JsonMapper.toList(jsonString, Ticker.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void importAvailableStock(String code, String status, String group, String date) {
        try {
            httpClient.sendGet(URL + "/availablestock/import/" + code + "/" + status + "/" + group + "/" + date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAllAvailableStock() {
        try {
            httpClient.sendGet(URL + "/availablestock/deleteall");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> findAvailableStockDateByGroup(String group) {
        try {
            String jsonString = httpClient.sendGet(URL + "/availablestock/getdatesbygroup/" + group);
            return JsonMapper.toStringList(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> findAvailableStockByGroupAndDate(String group, String date) {
        try {
            String jsonString = httpClient.sendGet(URL + "/availablestock/gettickerbydateandgroup/" + group + "/" + date);
            return JsonMapper.toStringList(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void importTicker(String ticker) {
        try {
            httpClient.sendGet(URL + "/yahooimportall/" + ticker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public List<String> findAvailableStockByGroup(String group) {
        try {
            String jsonString = httpClient.sendGet(URL + "/availablestock/getdatesbygroup/" + group);
            return JsonMapper.toStringList(jsonString);
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
        System.out.println(obj.findAvailableStockDateByGroup("BACKTESTING"));
        System.out.println(obj.findAvailableStockByGroupAndDate("BACKTESTING", "1999-12-06"));
    }
}
