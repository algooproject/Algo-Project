package com.algotrading.backtesting.pattern;

import java.text.ParseException;

public class PositionEqualSignal extends PositionSignal {

	public PositionEqualSignal(String expectedValueType, String expectedValue, double multiplier)
			throws ParseException {
		super(expectedValueType, expectedValue, multiplier);
	}

	@Override
	protected boolean determine(double value) {
		if (value > testValue * multiplier) {
			return true;
		} else {
			return false;
		}
	}

}
