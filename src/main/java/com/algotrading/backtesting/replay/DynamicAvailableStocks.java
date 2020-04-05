package com.algotrading.backtesting.replay;

import com.algotrading.backtesting.stock.Stock;

import java.util.Date;
import java.util.Map;

public interface DynamicAvailableStocks {
    AvailableStocks get(Date date);

    Map<String, Stock> getAllAvailableStocks();
}
