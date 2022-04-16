package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.io.StockFileGateway;
import com.algotrading.backtesting.stock.lotsize.LotSizeFileGateway;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TickerFileProvider implements TickerProvider {

    private final String resourcePath;
    private final String allStockListFilename;

    public TickerFileProvider(String resourcePath, String allStockListFilename) {
        this.resourcePath = resourcePath;
        this.allStockListFilename = allStockListFilename;
    }

    @Override
    public List<String> getAllTickers() throws StockCreationException {
        return getAllTickersFromFile();
    }

    @Override
    public Stock constructStockFromTickerString(String ticker) throws StockCreationException {
        Stock stock = new Stock( ticker );
        fillStockHistory(stock);
        return stock;
    }

    @Override
    public void fillStockHistory(Stock stock) throws StockCreationException {
        new StockFileGateway(resourcePath).fillData(stock);
    }

    @Override
    public int getLotSizeByTickerString(String ticker) {
        return new LotSizeFileGateway(resourcePath).getLotSize(ticker);
    }

    // allStockListPath: Constants.SRC_MAIN_RESOURCE_FILEPATH + "allStock.txt"
    private List<String> getAllTickersFromFile() throws StockCreationException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(resourcePath + allStockListFilename));
            String line;
            List<String> tickerList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    tickerList.add(line);
                }
            }
            return tickerList;
        } catch (IOException e) {
            throw new StockCreationException("Cannot get all tickers", e);
        }
    }

}
