package com.algotrading.backtesting.pattern;

import java.util.Date;

import com.algotrading.backtesting.stock.Stock;

public class MustFalseSignal implements StockSignal {

	@Override
	public boolean signal(Stock stock, Date date) {
		return false;
	}

}
