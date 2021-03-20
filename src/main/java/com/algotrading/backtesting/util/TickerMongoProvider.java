package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.tickerservice.TickerServiceClient;

import java.io.IOException;
import java.util.List;

public class TickerMongoProvider implements TickerProvider {

    private final TickerServiceClient tickerServiceClient;

    public TickerMongoProvider(TickerServiceClient tickerServiceClient) {
        this.tickerServiceClient = tickerServiceClient;
    }

    @Override
    public List<String> getAllTickers() throws IOException {
        return tickerServiceClient.getAllTickerStrings();
    }

    @Override
    public Stock constructStockFromTickerString(String ticker){
        Stock stock = new Stock( ticker );
        stock.readFromMongoDB();
        return stock;
    }
}
