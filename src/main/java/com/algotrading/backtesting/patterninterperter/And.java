package com.algotrading.backtesting.patterninterperter;

import com.algotrading.backtesting.pattern.AndPattern;
import com.algotrading.backtesting.pattern.IStockPattern;

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

	public IStockPattern execute() {
		return new AndPattern(left.execute(), right.execute());
	}
}