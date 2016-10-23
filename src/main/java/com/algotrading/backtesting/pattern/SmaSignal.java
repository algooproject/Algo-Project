package com.algotrading.backtesting.pattern;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.indicatorcalculator.SMA;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;

public abstract class SmaSignal implements StockSignal {

	protected int magnitude;
	protected double expectedValue;

	public SmaSignal(int magnitude, double expectedValue) {
		this.magnitude = magnitude;
		this.expectedValue = expectedValue;
	}

	public int getMagnitude() {
		return magnitude;
	}

	public double getExpectedValue() {
		return expectedValue;
	}

	@Override
	public boolean signal(Stock stock, Date date) {
		Map<Date, StockHistory> history = stock.getHistory();
		Map<Date, Double> closingHistory = new TreeMap<>();
		for (Map.Entry<Date, StockHistory> entry : history.entrySet()) {
			closingHistory.put(entry.getKey(), entry.getValue()
					.getClose());
		}
		try {
			SMA test = new SMA(closingHistory, date, magnitude);
			double value = test.getValue();
			return determine(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	protected abstract boolean determine(double value);
}
