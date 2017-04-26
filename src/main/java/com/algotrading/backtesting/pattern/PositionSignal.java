package com.algotrading.backtesting.pattern;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.algotrading.backtesting.indicatorcalculator.SMA;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;

public abstract class PositionSignal implements StockSignal {

	protected String expectedValueType;
	protected String expectedValue;
	protected double multiplier;
	protected double testValue;



	public PositionSignal(String expectedValueType, String expectedValue, double multiplier) {
		this.expectedValueType = expectedValueType;
		this.expectedValue = expectedValue;
		this.multiplier = multiplier;
	}

	public String getExpectedValueType() {
		return expectedValueType;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public double getMultiplier() {
		return multiplier;
	}

	@Override
	public boolean signal(Stock stock, Date date, Portfolio portfolio) throws ParseException {

		int value = portfolio.getStockQuantity(stock);
		settestValue();
		try {
			return determine(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		  }
		}


	protected abstract boolean determine(double value);

	private void settestValue() throws ParseException {
		switch (expectedValueType) {
		case "number":
			testValue = Double.parseDouble(this.expectedValue);
		case "variable":
			switch (expectedValue) { // no value for variable expectedValue 
			// case "closing":
				// testValue = closingHistory.get(date); 
			default:
				throw new ParseException("Invalid ExpectedvalueType -- " + expectedValue + ": no field match", 0);
			}
		default:
			throw new ParseException("Invalid ExpectedvalueType -- " + expectedValue + ": no field match", 0);
		}
	}
}
