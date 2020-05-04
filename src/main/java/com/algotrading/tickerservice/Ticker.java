package com.algotrading.tickerservice;

import com.algotrading.backtesting.stock.StockHistory;

import java.text.SimpleDateFormat;

public class Ticker {

	public String ticker;
	public String date;
	public double open;
	public double high;
	public double low;
	public double close;
	public double adjClose;
	public double volume;

	@Override
	public String toString() {
		return "Ticker{" + "ticker='" + ticker + '\'' + ", date='" + date + '\'' + ", open=" + open + ", high=" + high
				+ ", low=" + low + ", close=" + close + ", adjClose=" + adjClose + ", volume=" + volume + '}';
	}

	public StockHistory toStockHistory() {
		try {
			return new StockHistory(new SimpleDateFormat("yyyy-MM-dd").parse(date), open, close, high, low, adjClose, volume);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}