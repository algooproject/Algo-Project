package com.algotrading.backtesting.pattern;

public class OrPattern implements IStockPattern {

	private IStockPattern left;
	private IStockPattern right;
	
	public OrPattern(IStockPattern left, IStockPattern right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public boolean signal() {
		return left.signal() || right.signal();
	}
	
	@Override
	public String toString() {
		return "OR( " + left.toString() + " , " + right.toString() + " )";
	}

}
