package com.algotrading.backtesting.pattern;

public class SmaLowerThanSignal extends SmaSignal {

	public SmaLowerThanSignal(int magnitude, double expectedValue) {
		super(magnitude, expectedValue);
	}

	@Override
	protected boolean determine(double value) {
		if (value < expectedValue) {
			return true;
		} else {
			return false;
		}
	}

}
