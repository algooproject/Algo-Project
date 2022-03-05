package com.algotrading.backtesting.util;

import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.tickerservice.TickerServiceClient;

public class TickerProviderFactory {

    public static TickerProvider ofTickerProvider() {
        return AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB)
                ? new TickerMongoAllStocksProvider(new TickerServiceClient())
                : new TickerFileProvider(Constants.SRC_MAIN_RESOURCE_FILEPATH, "allStock.txt");
    }
}
