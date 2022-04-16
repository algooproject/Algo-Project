package com.algotrading.backtesting.stock.io;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.backtesting.util.StockCreationException;
import com.algotrading.tickerservice.Ticker;
import com.algotrading.tickerservice.TickerServiceClient;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class StockMongoDBGateway implements StockGateway {

    @Override
    public void fillTickerData(Stock stock)  throws StockCreationException {
        List<Ticker> tickers = new TickerServiceClient().findTickerByStockCode(stock.getTicker());
        tickers.sort(Comparator.comparing(ticker -> ticker.date));
//		tickers.sort(Comparator.comparing(tickersA -> tickersA.date)); // TODO to sort ascending or desending?
        if (tickers.size() == 0) {
            throw new StockCreationException("Stock " + stock.getTicker() + " not exist" );
        }
        Date earliestDate = stock.getEarliestDate();
        Date latestDate = stock.getLatestestDate();
        for (Ticker ticker : tickers) {
            try {
                Date date = Constants.DATE_FORMAT_YYYYMMDD.parse(ticker.date);

                if( earliestDate.equals( null ) || earliestDate.compareTo( date ) > 0 ) {
                    earliestDate = date;
                }
                if( latestDate.equals( null ) || latestDate.compareTo( date ) < 0 ) {
                    latestDate = date;
                }
                stock.getHistory().put(date, new StockHistory(date, ticker.open, ticker.close, ticker.high, ticker.low, ticker.adjClose, ticker.volume));
            } catch(Exception e) {
                throw new StockCreationException("Error when reading stock file", e);
            }
        }
        stock.setEarliestDate(earliestDate);
        stock.setLatestestDate(latestDate);
        stock.initialDate();
    }
}
