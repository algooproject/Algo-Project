package com.algotrading.backtesting.pattern;

import java.util.Date;

import com.algotrading.backtesting.stock.Stock;

public class SePattern implements IStockPattern {

	private String name;

	public SePattern(String name) {
		this.name = name;
	}

	@Override
	public boolean signal(Stock stock, Date date) {
		return false;
	}

	@Override
	public String toString() {
		return "SE[ " + name + " ]";
	}

}
