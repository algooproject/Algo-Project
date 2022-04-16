package com.algotrading.backtesting.stock.lotsize;

import com.algotrading.backtesting.replay.LotSize;

public class LotSizeFileGateway implements LotSizeGateway {

    private static final String LOT_SIZE_FILENAME = "lotSize.csv";

    private final String filePath;

    public LotSizeFileGateway(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int getLotSize(String code) {
        try {
            LotSize lotSize = new LotSize(filePath + LOT_SIZE_FILENAME);
            return lotSize.getLotSize(code);
        }
        catch (Exception e)
        {
            return -1;
        }
    }
}
