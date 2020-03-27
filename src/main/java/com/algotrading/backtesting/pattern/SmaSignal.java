package com.algotrading.backtesting.pattern;

//next step: -implement in a way that SMA of the same stock does not need to be created every time;
//           -may want to modify the way to get an element particular field (eg. Volume) using date. 

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
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
	protected Map<String, Map<Date, Double>> storedClosingHistory = new HashMap<String, Map<Date, Double>>();
	protected Map<Date, Double> closingHistory;
	protected Map<String, SMA> initiatedSMA = new HashMap<String, SMA>();
	protected SMA sma;

	private long initationTime = 0;
	private long numOfInitiation = 0;

	public SmaSignal(int magnitude, String expectedValueType, String expectedValue, double multiplier) {
		this.magnitude = magnitude;
		this.expectedValueType = expectedValueType;
		this.expectedValue = expectedValue;
		this.multiplier = multiplier;
//		this.closingHistory = new HashMap<Date, Double>();
		// settestValue();
	}

	public void setExpectedValue(String value) {
		expectedValue = value;
	}

	public Map<String, SMA> getInitiatedSMA() {
		return initiatedSMA;
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
	public boolean signal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch) throws ParseException {
		// Long startTime = System.nanoTime();

		boolean extracted = extracted(stock, date);
		// Long endTime = System.nanoTime();
		// initationTime = initationTime + (endTime - startTime) / 1000000;
		// if (numOfInitiation == 0 || numOfInitiation == 45367) {
		// System.out.println("SMA Accumulated Initiation Time = " + initationTime + ":"
		// + this.toString());
		// }
		// numOfInitiation++;
		// System.out.println("Number of Initiations = " + numOfInitiation);

		return extracted;
	}

	private boolean extracted(Stock stock, Date date) throws ParseException {
		if (initiatedSMA.get(stock.getTicker()) == null) {
			closingHistory = new TreeMap<Date, Double>();

			Map<Date, StockHistory> history = stock.getHistory();
			for (Map.Entry<Date, StockHistory> entry : history.entrySet()) {
//				System.out.println(entry.getKey().toString());
//				System.out.println(entry.getValue().getClose());
				closingHistory.put(entry.getKey(), entry.getValue().getClose());
			}
			storedClosingHistory.put(stock.getTicker(), closingHistory);
			try {
				sma = new SMA(closingHistory, date, magnitude);
				initiatedSMA.put(stock.getTicker(), sma);
//				System.out.println("Initiated sma.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			sma = initiatedSMA.get(stock.getTicker());
			closingHistory = storedClosingHistory.get(stock.getTicker());
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
				// System.out.println(date.toString() + " Hitting Closing");
//				System.out.println(closingHistory);
//				System.out.println(date);
//				System.out.println(closingHistory.get(date));
				if (closingHistory.get(date) == null) {
					testValue = -1.0;
				} else {
					testValue = closingHistory.get(date);
				}
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
