package com.algotrading.backtesting.pattern;

import java.util.Date;
import java.util.Map;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;

public abstract class ClosingSignal implements StockSignal {

	protected String expectedValueType; // "number", "variable" (not yet
										// support)
	protected String expectedValue; // number, "volume", "closing",
									// "holdingprice"
	protected int expectedLag; // positive integer
	protected double multiplier;
	protected double calExpectedValue;

	public ClosingSignal(String expectedValueType, String expectedValue, int expectedLag, double multiplier) {
		this.expectedValueType = expectedValueType;
		this.expectedValue = expectedValue;
		this.expectedLag = expectedLag;
		this.multiplier = multiplier;
	}

	public String getExpectedValueType() {
		return expectedValueType;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public int getExpectedLag() {
		return expectedLag;
	}

	public double getMultiplier() {
		return multiplier;
	}

	@Override
	public boolean signal(Stock stock, Date date) {
		Map<Date, StockHistory> history = stock.getHistory();
		calExpectedValue = Double.valueOf(expectedValue) * multiplier;
		try {
			double value = history.get(date).getClose();
			return determine(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	protected abstract boolean determine(double value);

}