package com.algotrading.backtesting.pattern;

public class ClosingHigherThanSignal extends ClosingSignal {

	public ClosingHigherThanSignal(String expectedValueType, String expectedValue, int expectedLag, double multiplier) {
		super(expectedValueType, expectedValue, expectedLag, multiplier);
		// System.out.println("closingHigherThanSignal initiated");
	}

	@Override
	protected boolean determine(double value) {
		// System.out.println("value = " + value);
		// System.out.println("calExpectedValue = " + calExpectedValue);
		if (value > calExpectedValue) {
			return true;
		} else {
			return false;
		}
	}

}
