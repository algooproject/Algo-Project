package com.algotrading.backtesting.pattern;

public class VolumeHigherThanSignal extends VolumeSignal {

	public VolumeHigherThanSignal(String expectedValueType, String expectedValue, int expectedLag, double multiplier) {
		super(expectedValueType, expectedValue, expectedLag, multiplier);
	}

	@Override
	protected boolean determine(double value) {
		if (Double.valueOf(calExpectedValue) == 0) {
			return false;
		} else if (value > Double.valueOf(calExpectedValue)) {
			return true;
		} else {
			return false;
		}
	}

}
