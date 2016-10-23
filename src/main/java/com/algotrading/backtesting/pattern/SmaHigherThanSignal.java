package com.algotrading.backtesting.pattern;

public class SmaHigherThanSignal extends SmaSignal {

	public SmaHigherThanSignal(int magnitude, double expectedValue) {
		super(magnitude, expectedValue);
	}

	@Override
	protected boolean determine(double value) {
		if (value > expectedValue) {
			return true;
		} else {
			return false;
		}
	}

}
