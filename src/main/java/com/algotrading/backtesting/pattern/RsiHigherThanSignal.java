package com.algotrading.backtesting.pattern;

import java.text.ParseException;

public class RsiHigherThanSignal extends RsiSignal {

	public RsiHigherThanSignal(int magnitude, int sma_magnitude, String expectedValueType, String expectedValue,
			double multiplier) throws ParseException {
		super(magnitude, expectedValueType, expectedValue, multiplier);
	}

	@Override
	protected boolean determine(double value) {
		if (value < 0)
			return false;
		if (value > testValue * multiplier)
			return true;
		return false;
	}

}
