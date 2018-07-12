package com.algotrading.backtesting.pattern;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;

public class ReentrySignal implements StockSignal {

	private StockSignal pattern;

	public ReentrySignal(StockSignal pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean signal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch) throws ParseException {
		if (stock.getStatus())
			return false;
		if (pattern.signal(stock, date, portfolio, buyCostIfMatch)) {
			stock.setStatus(true);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Reentry( " + pattern.toString() + " )";
	}

}
