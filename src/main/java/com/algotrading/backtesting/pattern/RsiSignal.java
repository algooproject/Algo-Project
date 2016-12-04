package com.algotrading.backtesting.pattern;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.indicatorcalculator.RSI;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;

public abstract class RsiSignal implements StockSignal {

	protected int magnitude;
	protected double expectedValue;
	protected int sma_magnitude;

	public RsiSignal(int magnitude, int sma_magnitude, double expectedValue) {
		this.magnitude = magnitude;
		this.sma_magnitude = sma_magnitude;
		this.expectedValue = expectedValue;
	}

	public int getMagnitude() {
		return magnitude;
	}

	public int getSMA_Magnitude() {
		return sma_magnitude;
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
			RSI test = new RSI(closingHistory, date, magnitude, sma_magnitude);
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
