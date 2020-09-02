package com.algotrading.backtesting.config;

import com.algotrading.backtesting.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class AlgoConfiguration {

    public static final String READ_STOCK_FROM = "readstockfrom";
    public static final String READ_AVAILABLE_STOCK_FROM = "readavailablestockfrom";
    public static final String AVAILABLE_STOCK_GROUP = "BACKTESTING";

    public static final String FROM_FILE = "file";
    public static final String FROM_MONGODB = "mongodb";


    // TODO modifiable
    public static final String STOCK_READ_PATH_IF_READ_FROM_FILE = Constants.SRC_MAIN_RESOURCE_FILEPATH;

    private static Map<String, String> data = new HashMap<>();


    public static void readAll(Map<String, String> dataMap) {
        data = dataMap;
    }

    public static void setReadAvailableStockFrom(String from) {
        if (from == null || (!from.equals(FROM_FILE) && !from.equals(FROM_MONGODB))) {
            throw new RuntimeException("Invalid value of " + READ_AVAILABLE_STOCK_FROM);
        }
        data.put(READ_AVAILABLE_STOCK_FROM, from);
    }

    public static void setReadStockFrom(String from) {
        if (from == null || (!from.equals(FROM_FILE) && !from.equals(FROM_MONGODB))) {
            throw new RuntimeException("Invalid value of " + READ_STOCK_FROM);
        }
        data.put(READ_STOCK_FROM, from);
    }

    public static String getReadStockFrom() {
        String value = data.get(READ_STOCK_FROM);
        return value != null ? value : FROM_FILE;
    }

    public static String getReadAvailableStockFrom() {
        String value = data.get(READ_AVAILABLE_STOCK_FROM);
        return value != null ? value : FROM_FILE;
    }

    public static String getAvailableStockGroup() {
        return AVAILABLE_STOCK_GROUP;
    }

}
