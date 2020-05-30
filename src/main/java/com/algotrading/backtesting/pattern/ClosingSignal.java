package com.algotrading.backtesting.pattern;

import java.util.Date;
import java.util.Map;

import com.algotrading.backtesting.portfolio.Portfolio;
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

	private long initationTime = 0;
	private int numOfInitiation = 0;

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
	public boolean signal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch) {

		// Long startTime = System.nanoTime();

		boolean extracted = extracted(stock, date, portfolio);
		// Long endTime = System.nanoTime();
		// initationTime = initationTime + (endTime - startTime) / 1000000;
		// if (numOfInitiation == 0 || numOfInitiation == 30562 || numOfInitiation ==
		// 30425) {
		// System.out.println("Closing Accumulated Initiation Time = " + initationTime +
		// ":" + this.toString());
		// }
		// numOfInitiation++;
		// System.out.println("Number of Initiations = " + numOfInitiation);

		return extracted;
	}

	private boolean extracted(Stock stock, Date date, Portfolio portfolio) {
		Double dblExpectedValue;
		if (!portfolio.containsStock(stock)) {
			// System.out.println("Not contain " + stock.getTicker());
			return false;
		}
		Map<Date, StockHistory> history = stock.getHistory();
		if (expectedValue.equals("holdingprice")) {
			// System.out.println("stock.getTicker()"+stock.getTicker());
			dblExpectedValue = portfolio.getPortfolioComponent(stock.getTicker()).getUnitPrice();
		} else {
			dblExpectedValue = Double.valueOf(expectedValue);
		}
		calExpectedValue = dblExpectedValue * multiplier;
		try {
			// System.out.println("getClose: " + date);
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
