package com.algotrading.backtesting.pattern;

public class AndPattern implements IStockPattern {

	private IStockPattern left;
	private IStockPattern right;
	
	public AndPattern(IStockPattern left, IStockPattern right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public boolean signal() {
		return left.signal() && right.signal();
	}
	
	@Override
	public String toString() {
		return "AND( " + left.toString() + " , " + right.toString() + " )";
	}

}
