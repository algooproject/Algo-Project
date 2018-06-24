package com.algotrading.backtesting.pattern;

import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;

public class isStockEnabledSignal implements StockSignal {

	public isStockEnabledSignal() {
	}

	@Override
	public boolean signal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch) {
		return stock.getStatus();
	}

}
