package com.algotrading.backtesting.pattern;

public class SePattern implements IStockPattern {

	private String name;
	
	public SePattern(String name) {
		this.name = name;
	}
	
	@Override
	public boolean signal() {
		return false;
	}
	
	@Override
	public String toString() {
		return "SE[ " + name + " ]";
	}

}
