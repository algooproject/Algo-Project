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

    public Map<String, Double> getAllRSIEntries(String stockCode) {
        try {
            String jsonString = httpClient.sendGet(URL + "/rsi/findrsientrymapbycode/" + stockCode);
            return JsonMapper.toPrimitiveMap(jsonString, Double.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RSIEntry> findRSIEntryByStockCode(String stockCode) {
        try {
            String jsonString = httpClient.sendGet(URL + "/rsi/findrsientrybycode/" + stockCode);
            return JsonMapper.toList(jsonString, RSIEntry.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllTickerStrings() {
        try {
            String jsonString = httpClient.sendGet(URL + "/ticker/getalltickers/");
            return JsonMapper.toList(jsonString, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Ticker> findTickerByStockCode(String stockCode) {
        try {
            String jsonString = httpClient.sendGet(URL + "/ticker/findtickerbycode/" + stockCode);
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

    public void importTickersByStockCode(String stockCode) {
        try {
            httpClient.sendGet(URL + "/yahooimportall/" + stockCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void importLotSize(String ticker, String lotSize) {
        try {
            httpClient.sendGet(URL + "/ticker/createlotsize/" + ticker + "/" + lotSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public int getLotSizeByStockCode(String code) {
        try {
            String lotSize = httpClient.sendGet(URL + "/ticker/findlotsizebycode/" + code);
            return Integer.parseInt(lotSize);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
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
        System.out.println(obj.getLotSizeByStockCode("6858.HK"));
        System.out.println(obj.getAllRSIEntries("2800.HK,2821.HK"));
        System.out.println(obj.findRSIEntryByStockCode("2800.HK"));
        System.out.println(obj.findTickerByStockCode("2800.HK"));
        System.out.println(obj.findAvailableStockDateByGroup("BACKTESTING"));
        System.out.println(obj.findAvailableStockByGroupAndDate("BACKTESTING", "1999-12-06"));
    }
}
