package com.algotrading.backtesting.patterninterperter;

import com.algotrading.backtesting.pattern.AndSignal;
import com.algotrading.backtesting.pattern.StockSignal;

public class And implements Node {
	private Node left;
	private Node right;
	private String name;
	private boolean parsedLeft = false;

	public void parse(Context context) throws ParseException {
		name = context.currentToken();
		context.skipToken(name);
		if (!(name.equals("AND("))) {
			throw new ParseException();
		}
		while (true) {
			if (context.currentToken() == null) {
				throw new ParseException();
			} else if (context.currentToken().equals(")")) {
				context.skipToken(")");
				break;
			} else if (context.currentToken().equals(",")) {
				context.skipToken(",");
				parsedLeft = true;
			} else {
				Node expr = new Expr();
				expr.parse(context);
				if (!parsedLeft) {
					left = expr;
				} else {
					right = expr;
				}
			}
		}
	}

	public StockSignal execute() {
		return new AndSignal(left.execute(), right.execute());
	}
}