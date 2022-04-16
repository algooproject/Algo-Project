package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.io.StockMongoDBGateway;
import com.algotrading.backtesting.stock.lotsize.LotSizeMongoDBGateway;
import com.algotrading.tickerservice.TickerServiceClient;

import java.util.List;

public class TickerMongoAvailableStocksProvider implements TickerProvider {

    private static final String BACKTESTING_GROUP_NAME = "BACKTESTING";

    private final TickerServiceClient tickerServiceClient;

    private final String stockListFileName;

    private final boolean hasDate;

    public TickerMongoAvailableStocksProvider(String stockListFileName, boolean hasDate, TickerServiceClient tickerServiceClient) {
        this.stockListFileName = stockListFileName;
        this.hasDate = hasDate;
        this.tickerServiceClient = tickerServiceClient;
    }

    @Override
    public List<String> getAllTickers() throws StockCreationException {
        return hasDate
                ? tickerServiceClient.findAvailableStockByGroupAndDate(BACKTESTING_GROUP_NAME, stockListFileName)
                : tickerServiceClient.findAvailableStockByGroup(BACKTESTING_GROUP_NAME);
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
