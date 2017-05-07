package com.algotrading.backtesting.pattern;

//next step: -implement in a way that SMA of the same stock does not need to be created every time;
//           -may want to modify the way to get an element particular field (eg. Volume) using date. 

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.indicatorcalculator.SMA;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;

public abstract class SmaSignal implements StockSignal {

	protected int magnitude = 10;
	protected String expectedValueType;
	protected String expectedValue;
	protected double multiplier;
	protected double testValue;
	protected Map<Date, Double> closingHistory;
	protected SMA sma;

	public SmaSignal(int magnitude, String expectedValueType, String expectedValue, double multiplier) {
		this.magnitude = magnitude;
		this.expectedValueType = expectedValueType;
		this.expectedValue = expectedValue;
		this.multiplier = multiplier;
//		this.closingHistory = new HashMap<Date, Double>();
		// settestValue();
	}

	public int getMagnitude() {
		return magnitude;
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
		if (closingHistory == null) {
			closingHistory = new TreeMap<Date, Double>();
			
			Map<Date, StockHistory> history = stock.getHistory();
			for (Map.Entry<Date, StockHistory> entry : history.entrySet()) {
//				System.out.println(entry.getKey().toString());
//				System.out.println(entry.getValue().getClose());
				closingHistory.put(entry.getKey(), entry.getValue().getClose());
			}
			try {
				sma = new SMA(closingHistory, date, magnitude);
//				System.out.println("Initiated sma.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		sma.setRecent(date);
		settestValue(date);
		try {
			double value = sma.getValue();
			return determine(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	protected abstract boolean determine(double value);

	private void settestValue(Date date) throws ParseException {
		switch (expectedValueType) {
		case "number":
			testValue = Double.parseDouble(this.expectedValue);
			break;
		case "variable":
			switch (expectedValue) {
			case "closing":
//				System.out.println(date.toString() + " Hitting Closing");
				testValue = closingHistory.get(date); 
				// should depend on expectedValue
				break;
			default:
				throw new ParseException("Invalid Expectedvalue -- " + expectedValue + ": no field match", 0);
			}
			break;
		default:
			throw new ParseException("Invalid ExpectedvalueType -- " + expectedValueType + ": no field match", 0);
		}
	}
}
