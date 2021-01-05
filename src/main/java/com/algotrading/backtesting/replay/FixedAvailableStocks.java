package com.algotrading.backtesting.replay;

import com.algotrading.backtesting.stock.Stock;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class FixedAvailableStocks implements DynamicAvailableStocks {

    /** true = set to read specific path when reading stock, but not from resourceAsStream */
    private boolean isCustomizedStockFilePath = false;

    private AvailableStocks availableStocks;

    public FixedAvailableStocks(String filePath, String fileName) {
        this.availableStocks = new AvailableStocks(filePath, fileName, false);
    }

    public FixedAvailableStocks(AvailableStocks availableStocks) {
        this.availableStocks = availableStocks;
    }

    @Override
    public void load() throws IOException, ParseException
    {
        if (isCustomizedStockFilePath) {
            availableStocks.customizedStockFilePath();
        }
        this.availableStocks.load();
    }

    public AvailableStocks get(Date date) {
        return availableStocks;
    }

    public Map<String, Stock> getAllAvailableStocks() {
        return availableStocks.getAllAvailableStocks();
    }

    @Override
    public void customizedStockFilePath() {
        isCustomizedStockFilePath = true;
    }
}
