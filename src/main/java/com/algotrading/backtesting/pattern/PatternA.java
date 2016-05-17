package com.algotrading.backtesting.pattern;

import java.math.BigDecimal;
import java.util.Date;

import com.algotrading.backtesting.stock.IStock;

public class PatternA implements IStockPattern {

	private IStock stock;
	
	public PatternA(IStock stock) {
		this.stock = stock;
	}
	
	@Override
	public boolean signal() {
		// TODO Auto-generated method stub
		return stock.RSI(today(), 14).compareTo(new BigDecimal(30)) < 0;
	}

	private Date today() {
		// TODO Auto-generated method stub
		return null;
	}

}
