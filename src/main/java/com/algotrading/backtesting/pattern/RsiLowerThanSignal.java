package com.algotrading.backtesting.pattern;

public class RsiLowerThanSignal extends RsiSignal {

	public RsiLowerThanSignal(int magnitude, int sma_magnitude, double expectedValue) {
		super(magnitude, sma_magnitude, expectedValue);
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
