package com.algotrading.stockanalysis.model.stock;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

public class YahooStockPool {

	private Map<String, Stock> stockPool;
	private Map<String, Map<String, HistoricalQuote>> historicalQuote;
	
	public YahooStockPool() {
		stockPool = new HashMap<>();
		historicalQuote = new HashMap<>();
	}
	
	public void put(String ticker, Stock stock,  Map<String, HistoricalQuote> history) {
		stockPool.put(ticker, stock);
		historicalQuote.put(ticker, history);
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
}
