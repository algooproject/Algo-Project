package com.algotrading.backtesting.pattern;

import java.util.Date;

import com.algotrading.backtesting.stock.Stock;

public class AndPattern implements IStockPattern {

	private IStockPattern left;
	private IStockPattern right;

	public AndPattern(IStockPattern left, IStockPattern right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean signal(Stock stock, Date date) {
		return left.signal(stock, date) && right.signal(stock, date);
	}

	@Override
	public String toString() {
		return "AND( " + left.toString() + " , " + right.toString() + " )";
	}

}
