package com.algotrading.backtesting.pattern;

import java.util.Date;

import com.algotrading.backtesting.stock.Stock;

public class NotPattern implements IStockPattern {

	private IStockPattern pattern;

	public NotPattern(IStockPattern pattern) {
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
