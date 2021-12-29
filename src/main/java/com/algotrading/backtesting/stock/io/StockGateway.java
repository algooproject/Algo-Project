package com.algotrading.backtesting.stock.io;

import com.algotrading.backtesting.stock.Stock;

public interface StockGateway {

    /**
     * This method expect receive a empty stock only with ticker, and fill data by different IO e.g. file / db).
     * Return true if the stock is filled successfully (e.g. db has related value)
     */
    boolean fillData(Stock stock);
}
