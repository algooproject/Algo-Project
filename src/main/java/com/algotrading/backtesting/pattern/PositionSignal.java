package com.algotrading.backtesting.pattern;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;

public abstract class PositionSignal implements StockSignal {

	protected String expectedValueType;
	protected String expectedValue;
	protected double multiplier;
	protected double testValue;

	public PositionSignal(String expectedValueType, String expectedValue, double multiplier) throws ParseException {
		this.expectedValueType = expectedValueType;
		this.expectedValue = expectedValue;
		this.multiplier = multiplier;
		settestValue();
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
	public boolean signal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch) throws ParseException {

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
			// System.out.println("Hitted number!!!");
			try {
				testValue = Double.parseDouble(expectedValue);
			} catch (Exception e) {
				throw new ParseException(expectedValue + " cannot be converted into double", 0);
			}

			break;
		case "variable":
			switch (expectedValue) { // no value for variable expectedValue
			case "closing":
				throw new ParseException("Getting closing price is not implemented yet", 0);
				// testValue = closingHistory.get(date);
			default:
				throw new ParseException("Invalid Expectedvalue -- " + expectedValue + ": no field match", 0);
			}
		default:
			throw new ParseException("Invalid ExpectedvalueType -- " + expectedValueType + ": no field match", 0);
		}
	}
}
