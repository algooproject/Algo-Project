package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;

import java.util.List;

public interface TickerProvider {

    List<String> getAllStockCodes() throws StockCreationException;

    Stock constructStockFromStockCode(String ticker) throws StockCreationException;

    void fillStockHistory(Stock stock) throws StockCreationException;

    int getLotSizeByStockCode(String ticker) throws StockCreationException;

    default Stock constructStockWithLotSizeFromTickerString(String ticker) throws StockCreationException {
        Stock stock = new Stock(ticker, getLotSizeByStockCode(ticker));
        fillStockHistory(stock);
        return stock;
    }

}
