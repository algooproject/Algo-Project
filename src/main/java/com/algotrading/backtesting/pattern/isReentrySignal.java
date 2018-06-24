package com.algotrading.backtesting.pattern;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;

public abstract class isReentrySignal implements StockSignal {

	public isReentrySignal() {
	}

	@Override
	public boolean signal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch) throws ParseException {
		if (stock.getStatus())
			return false;
		return secondSignal(stock, date, portfolio, buyCostIfMatch);
	}

	protected abstract boolean secondSignal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch)
			throws ParseException;

	protected abstract boolean determine(double value);

}
