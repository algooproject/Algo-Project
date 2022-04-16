package com.algotrading.backtesting.stock.io;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.StockCreationException;

public interface StockGateway {

    /**
     * This method expect receive a empty stock only with ticker, and fill data by different IO e.g. file / db).
     */
    void fillData(Stock stock) throws StockCreationException;
}
