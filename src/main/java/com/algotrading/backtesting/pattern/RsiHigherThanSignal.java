package com.algotrading.backtesting.pattern;

import java.text.ParseException;

public class RsiHigherThanSignal extends RsiSignal {

	public RsiHigherThanSignal(int magnitude, int sma_magnitude, String expectedValueType, String expectedValue,
			double multiplier) throws ParseException {
		super(magnitude, sma_magnitude, expectedValueType, expectedValue, multiplier);
	}

	@Override
	protected boolean determine(double value) {
		// TODO: broken
		// if (value > expectedValue) {
		// return true;
		// } else {
		// return false;
		// }
		return false;
	}

}
