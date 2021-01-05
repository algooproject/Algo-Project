package com.algotrading.backtesting.replay;

import com.algotrading.backtesting.stock.Stock;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public interface DynamicAvailableStocks {
    AvailableStocks get(Date date);

    Map<String, Stock> getAllAvailableStocks();

    void load() throws IOException, ParseException;

    void customizedStockFilePath();
}
