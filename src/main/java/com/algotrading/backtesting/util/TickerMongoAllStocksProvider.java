package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.io.StockMongoDBGateway;
import com.algotrading.backtesting.stock.lotsize.LotSizeMongoDBGateway;
import com.algotrading.tickerservice.TickerServiceClient;

import java.util.List;

public class TickerMongoAllStocksProvider implements TickerProvider {

    private final TickerServiceClient tickerServiceClient;

    public TickerMongoAllStocksProvider(TickerServiceClient tickerServiceClient) {
        this.tickerServiceClient = tickerServiceClient;
    }

    @Override
    public List<String> getAllStockCodes() throws StockCreationException {
        return tickerServiceClient.getAllTickerStrings();
    }

    @Override
    public Stock constructStockFromStockCode(String code) throws StockCreationException {
        Stock stock = new Stock(code);
        fillStockHistory(stock);
        return stock;
    }

    @Override
    public void fillStockHistory(Stock stock) throws StockCreationException {
        new StockMongoDBGateway().fillTickerData(stock);
    }

    @Override
    public int getLotSizeByStockCode(String code) {
        return new LotSizeMongoDBGateway().getLotSize(code);
    }
}
