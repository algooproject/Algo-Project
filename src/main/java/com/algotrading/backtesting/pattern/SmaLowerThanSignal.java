package com.algotrading.backtesting.pattern;

import java.text.ParseException;

public class SmaLowerThanSignal extends SmaSignal {

	public SmaLowerThanSignal(int magnitude, String expectedValueType, String expectedValue, double multiplier)
			throws ParseException {
		super(magnitude, expectedValueType, expectedValue, multiplier);

	}

	@Override
	protected boolean determine(double value) {
		// TODO broken
		// if (value < expectedValue) {
		// return true;
		// } else {
		// return false;
		// }
		return false;
	}

}
