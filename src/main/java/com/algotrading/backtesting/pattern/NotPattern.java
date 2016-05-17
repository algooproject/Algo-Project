package com.algotrading.backtesting.pattern;

public class NotPattern implements IStockPattern {
	
	private IStockPattern pattern;
	
	public NotPattern(IStockPattern pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public boolean signal() {
		return !pattern.signal();
	}
	
	@Override
	public String toString() {
		return "NOT( " + pattern.toString() + " )";
	}

}
