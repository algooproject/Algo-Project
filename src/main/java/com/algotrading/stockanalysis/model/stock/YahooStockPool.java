package com.algotrading.stockanalysis.model.stock;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

public class YahooStockPool {

	private Map<String, Stock> stockPool;
	private Map<String, Map<String, HistoricalQuote>> historicalQuote;
	private Map<String, Calendar> fromPool;
	private Map<String, Calendar> toPool;
	
	public YahooStockPool() {
		stockPool = new HashMap<>();
		historicalQuote = new HashMap<>();
		fromPool = new HashMap<>();
		toPool = new HashMap<>();
	}
	
	public void put(String ticker, Stock stock,  Map<String, HistoricalQuote> history, Calendar from, Calendar to) {
		stockPool.put(ticker, stock);
		historicalQuote.put(ticker, history);
		fromPool.put(ticker, from);
		toPool.put(ticker, to);
	}
	
	public boolean stockExist(String ticker) {
		return stockPool.get(ticker) != null && historicalQuote.get(ticker) != null;
	}
	
	public Stock getStock(String ticker) {
		return stockPool.get(ticker);
	}
	
	public Map<String, HistoricalQuote> getStockHistory(String ticker) {
		return historicalQuote.get(ticker);
	}

	public Calendar getTo(String ticker) {
		return toPool.get(ticker);
	}

	public Calendar getFrom(String ticker) {
		return fromPool.get(ticker);
	}
}
