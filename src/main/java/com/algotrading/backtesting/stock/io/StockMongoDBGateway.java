package com.algotrading.backtesting.stock.io;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.tickerservice.Ticker;
import com.algotrading.tickerservice.TickerServiceClient;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class StockMongoDBGateway implements StockGateway {

    @Override
    public boolean fillData(Stock stock) {
        List<Ticker> tickers = new TickerServiceClient().findTickerByCode(stock.getTicker());
        tickers.sort(Comparator.comparing(ticker -> ticker.date));
//		tickers.sort(Comparator.comparing(tickersA -> tickersA.date)); // TODO to sort ascending or desending?
        if (tickers.size() == 0) {
            return false;
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
                e.printStackTrace();
            }
        }
        stock.setEarliestDate(earliestDate);
        stock.setLatestestDate(latestDate);
        stock.initialDate();
        return true;
    }
}
