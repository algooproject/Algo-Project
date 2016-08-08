package com.algotrading.backtesting.pattern;

import java.util.Date;

import com.algotrading.backtesting.stock.Stock;

public class NotSignal implements StockSignal {

	private StockSignal pattern;

	public NotSignal(StockSignal pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean signal(Stock stock, Date date) {
		return !pattern.signal(stock, date);
	}

	@Override
	public String toString() {
		return "NOT( " + pattern.toString() + " )";
	}

}
