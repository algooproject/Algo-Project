package com.algotrading.backtesting.stock.lotsize;

import com.algotrading.tickerservice.TickerServiceClient;

public class LotSizeMongoDBGateway implements LotSizeGateway {
    @Override
    public int getLotSize(String code) {
        return new TickerServiceClient().getLotSizeByStockCode(code);
    }
}
