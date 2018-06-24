package com.algotrading.backtesting.pattern;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;

public class isReentryByRsiHigherThanSignal extends isReentrySignal {

	private RsiHigherThanSignal signal;
	protected String expectedValueType; // "number", "variable" (not yet
	// support)
	protected String expectedValue; // number, "volume", "closing",
									// "holdingprice"
	protected double multiplier;
	protected double calExpectedValue;

	public isReentryByRsiHigherThanSignal(int magnitude, int sma_magnitude, String expectedValueType, String expectedValue,
			double multiplier) throws ParseException {
		super();
		signal = new RsiHigherThanSignal(magnitude, sma_magnitude, expectedValueType, expectedValue, multiplier);
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
	protected boolean secondSignal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch)
			throws ParseException {
		if (signal.signal(stock, date, portfolio, buyCostIfMatch)) {
			// System.out.println("isReentrySignal: stock " + stock.getTicker()
			// + " is enabled on " + date.toString());
			stock.setStatus(true);
			return true;
		}
		return false;
	}

	@Override
	protected boolean determine(double value) {
		return true;
	}

}
