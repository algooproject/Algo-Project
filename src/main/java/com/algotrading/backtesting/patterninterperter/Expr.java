package com.algotrading.backtesting.patterninterperter;

import com.algotrading.backtesting.pattern.StockSignal;

public class Expr implements Node {
	private Node node;

	public void parse(Context context) throws ParseException {
		if (context.currentToken().equals("AND(")) {
			node = new And();
			node.parse(context);
		} else if (context.currentToken().equals("OR(")) {
			node = new Or();
			node.parse(context);
		} else if (context.currentToken().equals("NOT(")) {
			node = new Not();
			node.parse(context);
		} else if (context.currentToken().equals("SE[")) {
			node = new Se();
			node.parse(context);
		} else {
			throw new ParseException();
		}
	}

	public StockSignal execute() {
		return node.execute();
	}
}
