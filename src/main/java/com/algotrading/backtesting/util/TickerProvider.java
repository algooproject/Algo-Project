package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;

import java.util.List;

public interface TickerProvider {

    List<String> getAllTickers() throws StockCreationException;

    Stock constructStockFromTickerString(String ticker) throws StockCreationException;

    void fillStockHistory(Stock stock) throws StockCreationException;

    int getLotSizeByTickerString(String ticker) throws StockCreationException;

    default Stock constructStockWithLotSizeFromTickerString(String ticker) throws StockCreationException {
        Stock stock = new Stock(ticker, getLotSizeByTickerString(ticker));
        fillStockHistory(stock);
        return stock;
    }

}
