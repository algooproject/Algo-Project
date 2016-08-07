package com.algotrading.backtesting.stock;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Stock {

	private static String FILEPATH = "src/main/resources";
	private String ticker;
	private Map<Date, StockHistory> history;

	public Stock(String ticker) {
		this.ticker = ticker;
		history = new TreeMap<>();
	}

	public void read() {
		// TODO read files from ticker
	}

	public String getTicker() {
		return ticker;
	}

	public Map<Date, StockHistory> getHistory() {
		return history;
	}

}
