package com.algotrading.backtesting.replay;

import com.algotrading.backtesting.stock.Stock;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class FixedAvailableStocks implements DynamicAvailableStocks {

    private AvailableStocks availableStocks;

    public FixedAvailableStocks(String filePath, String stockListName) throws IOException, ParseException {
        this.availableStocks = new AvailableStocks(filePath, stockListName, false);
    }

    public FixedAvailableStocks(AvailableStocks availableStocks) {
        this.availableStocks = availableStocks;
    }

    public AvailableStocks get(Date date) {
        return availableStocks;
    }

    public Map<String, Stock> getAllAvailableStocks() {
        return availableStocks.getAllAvailableStocks();
    }



}
