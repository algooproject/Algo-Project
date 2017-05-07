package com.algotrading.backtesting.pattern;

import java.text.ParseException;

public class PositionEqualSignal extends PositionSignal {

	public PositionEqualSignal(String expectedValueType, String expectedValue, double multiplier)
			throws ParseException {
		super(expectedValueType, expectedValue, multiplier);
	}

	@Override
	protected boolean determine(double value) {

		if (Math.abs(testValue - value * multiplier ) < 1e-4) {
			return true;
		} else {
			return false;
		}
	}

}
