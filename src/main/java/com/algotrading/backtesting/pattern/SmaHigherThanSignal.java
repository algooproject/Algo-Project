package com.algotrading.backtesting.pattern;

import java.text.ParseException;

public class SmaHigherThanSignal extends SmaSignal {
	protected String expectedValueType = "number";
	private String expectedValue;
	private double multiplier;
	private double testValue;
	
	public SmaHigherThanSignal(int magnitude, String expectedValueType, String expectedValue, double multiplier ) throws ParseException {
		super(magnitude);
		this.expectedValueType = expectedValueType;
		this.expectedValue = expectedValue;
		settestValue();
	}

	@Override
	protected boolean determine(double value) {
		if (value > testValue * multiplier) {
			return true;
		} else {
			return false;
		}
	}
	
	private void settestValue() throws ParseException{
		switch (expectedValueType){
		case "number": testValue = Double.parseDouble(this.expectedValue);
		case "variable": testValue = closingPrices.get(date);
		default: throw new ParseException("Invalid Expectedvaluetype -- " + expectedValue + ": no field match", 0);
		}
	}
}

