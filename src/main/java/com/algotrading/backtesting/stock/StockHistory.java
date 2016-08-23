package com.algotrading.backtesting.stock;

import java.util.Date;

public class StockHistory {

	private Date date;
	private double high;
	private double low;
	private double open;
	private double close;
	private double adjClose;
	private double volume;

	public StockHistory(Date date, double open, double close, double high, double low, double adjClose, double volume) {
		this.date = date;
		this.high = high;
		this.low = low;
		this.open = open;
		this.close = close;
		this.adjClose = adjClose;
		this.volume = volume;
	}

	public Date getDate() {
		return date;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getOpen() {
		return open;
	}

	public double getClose() {
		return close;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public double getVolume() {
		return volume;
	}

}
