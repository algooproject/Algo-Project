package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.io.StockFileGateway;

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
    public List<String> getAllTickers() throws IOException {
        return getAllTickersFromFile();
    }

    @Override
    public Stock constructStockFromTickerString(String ticker){
        Stock stock = new Stock( ticker );
        fillStockHistory(stock);
        return stock;
    }

    @Override
    public boolean fillStockHistory(Stock stock){
        return new StockFileGateway(resourcePath).fillData(stock);
    }

    // allStockListPath: Constants.SRC_MAIN_RESOURCE_FILEPATH + "allStock.txt"
    private List<String> getAllTickersFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(resourcePath + allStockListFilename));
        String line;
        List<String> tickerList = new ArrayList<>();
        while ((line = br.readLine()) != null)
        {
            if( !line.trim().isEmpty() && !line.startsWith( "#" ) ) {
                tickerList.add(line);
            }
        }
        return tickerList;
    }

}
