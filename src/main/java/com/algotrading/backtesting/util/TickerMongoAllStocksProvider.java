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
    public List<String> getAllTickers() throws StockCreationException {
        return tickerServiceClient.getAllTickerStrings();
    }

    @Override
    public Stock constructStockFromTickerString(String ticker) throws StockCreationException {
        Stock stock = new Stock( ticker );
        fillStockHistory(stock);
        return stock;
    }

    @Override
    public void fillStockHistory(Stock stock) throws StockCreationException {
        new StockMongoDBGateway().fillData(stock);
    }

    @Override
    public int getLotSizeByTickerString(String ticker) {
        return new LotSizeMongoDBGateway().getLotSize(ticker);
    }
}
