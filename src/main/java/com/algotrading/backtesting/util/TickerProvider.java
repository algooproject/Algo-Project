package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;

import java.io.IOException;
import java.util.List;

public interface TickerProvider {

    List<String> getAllTickers() throws IOException;

    Stock constructStockFromTickerString(String ticker);

    /** return true if the source of read file exist */
    boolean fillStockHistory(Stock stock);

}
