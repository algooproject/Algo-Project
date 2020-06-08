package com.algotrading.backtesting.config;

import java.util.HashMap;
import java.util.Map;

public class AlgoConfiguration {

    public static final String READ_STOCK_FROM = "readstockfrom";
    public static final String READ_STOCK_FROM_FILE = "file";
    public static final String READ_STOCK_FROM_MONGODB = "mongodb";

    private static Map<String, String> data = new HashMap<>();


    public static void readAll(Map<String, String> dataMap) {
        data = dataMap;
    }

    public static void setReadStockFrom(String from) {
        if (from == null || !from.equals(READ_STOCK_FROM_FILE) || !from.equals(READ_STOCK_FROM_MONGODB)) {
            throw new RuntimeException("Invalid value of " + READ_STOCK_FROM);
        }
        data.put(READ_STOCK_FROM, from);
    }

    public static String getReadStockFrom() {
        String value = data.get(READ_STOCK_FROM);
        return value != null ? value : READ_STOCK_FROM_FILE;
    }


}
