package com.algotrading.backtesting.pattern;

// next step: -implement in a way that RSI of the same stock does not need to be created every time. 
//            -may want to modify the way to get an element particular field (eg. Volume) using date.
// find the index of an element of a list: List.indexOf(elm)
// Not = !
// How should we write in buyStrategies.txt

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.indicatorcalculator.RSI;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;

public abstract class RsiSignal implements StockSignal {

	protected int magnitude;
	// protected int sma_magnitude;
	protected String expectedValueType;
	protected String expectedValue;
	protected double multiplier;
	protected double testValue;

	protected Map<String, Map<Date, Double>> storedClosingHistory = new HashMap<String, Map<Date, Double>>();
	protected Map<Date, Double> closingHistory;
	protected Map<String, RSI> initiatedRSI = new HashMap<String, RSI>();
	protected RSI rsi;

	private long initationTime = 0;
	private int numOfInitiation = 0;

	// public RsiSignal(int magnitude, int sma_magnitude, String
	// expectedValueType, String expectedValue, double multiplier) throws
	// ParseException {
	public RsiSignal(int magnitude, String expectedValueType, String expectedValue, double multiplier)
			throws ParseException {

		this.magnitude = magnitude;
		// this.sma_magnitude = sma_magnitude;
		// this.expectedValueType = expectedValue; this is a bug; corrected 10
		// Sep
		this.expectedValueType = expectedValueType;
		this.expectedValue = expectedValue;
		this.multiplier = multiplier;
		// settestValue();
	}

	public void setExpectedValue(String value) {
		expectedValue = value;
	}

	public Map<String, RSI> getInitiatedRSI() {
		return initiatedRSI;
	}

	public int getMagnitude() {
		return magnitude;
	}

	// public int getSMA_Magnitude() {
	// return sma_magnitude;
	// }

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
		// Long startTime = System.nanoTime();

		boolean extracted = extracted(stock, date);
		// Long endTime = System.nanoTime();
		// initationTime = initationTime + (endTime - startTime) / 1000000;
		// if (numOfInitiation == 0 || numOfInitiation == 17407 || numOfInitiation ==
		// 167757) {
		// System.out.println("RSI Accumulated Initiation Time = " + initationTime + ":"
		// + this.toString());
		// }
		// numOfInitiation++;
		// System.out.println("Number of Initiations = " + numOfInitiation);

		return extracted;
	}

	private boolean extracted(Stock stock, Date date) {
		// System.out.println(stock.getTicker());
		if (initiatedRSI.get(stock.getTicker()) == null) {
			Map<Date, StockHistory> history = stock.getHistory();
			closingHistory = new TreeMap<>();
			for (Map.Entry<Date, StockHistory> entry : history.entrySet()) {
				// System.out.println(entry.getKey().toString() + '/' +
				// entry.getValue().getClose());
				closingHistory.put(entry.getKey(), entry.getValue().getClose());
			}
			// System.out.println(closingHistory.size());
			try {
				// rsi = new RSI(closingHistory, date, magnitude,
				// sma_magnitude);
				// System.out.println(stock.getTicker() + "; Magnitude = " + magnitude);
				rsi = new RSI(closingHistory, date, magnitude);

				initiatedRSI.put(stock.getTicker(), rsi);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			rsi = initiatedRSI.get(stock.getTicker());
			closingHistory = storedClosingHistory.get(stock.getTicker());
		}
		try {
			// RSI rsi = new RSI(closingHistory, date, magnitude,
			// sma_magnitude);
			rsi.setRecent(date);
			// System.out.println("date: " + date.toString());
			settestValue(date);
			double value = rsi.getValue();
			// System.out.println("rsi.getValue(): " + rsi.getValue());
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
			break; // missing breaks; corrected 10 Sep 2017
		case "variable":
			switch (expectedValue) {
			case "closing":
				testValue = closingHistory.get(date); // should depend on
				break; // expectedValue
			default:
				throw new ParseException("Invalid ExpectedvalueType -- " + expectedValueType + ": no field match", 0);
			}
			break;
		default:
			throw new ParseException("Invalid ExpectedvalueType -- " + expectedValue + ": no field match", 0);
		}
	}
}
