package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TickerFileProvider implements TickerProvider {

    private final String allStockListPath;

    public TickerFileProvider(String allStockListPath) {
         this.allStockListPath = allStockListPath;
    }

    @Override
    public List<String> getAllTickers() throws IOException {
        return getAllTickersFromFile();
    }

    @Override
    public Stock constructStockFromTickerString(String ticker){
        Stock stock = new Stock( ticker );
        stock.readFromFile(Constants.SRC_MAIN_RESOURCE_FILEPATH);
        return stock;
    }

    // allStockListPath: Constants.SRC_MAIN_RESOURCE_FILEPATH + "allStock.txt"
    private List<String> getAllTickersFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(allStockListPath));
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
