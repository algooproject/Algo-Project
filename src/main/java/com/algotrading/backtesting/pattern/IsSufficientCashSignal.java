package com.algotrading.backtesting.pattern;

import java.text.ParseException;

public class IsSufficientCashSignal extends CashSignal{

	public IsSufficientCashSignal() throws ParseException {
		super();
	}

	@Override
	protected boolean determine() {
		return currentCash >= buyCostIfMatch;
	}

	@Override
	public String toString() {
		return "isSufficientCash[]";
	}	
	
	@Override
	protected void settestValue() throws ParseException {}
	
}
