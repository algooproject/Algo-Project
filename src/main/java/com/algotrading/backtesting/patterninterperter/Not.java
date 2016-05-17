package com.algotrading.backtesting.patterninterperter;

import com.algotrading.backtesting.pattern.IStockPattern;
import com.algotrading.backtesting.pattern.NotPattern;

public class Not implements Node {
	private Node node;
	private String name;

	public void parse(Context context) throws ParseException {
		name = context.currentToken();
		context.skipToken(name);
		if (!(name.equals("NOT("))) {
			throw new ParseException();
		}
		while (true) {
			if (context.currentToken() == null) {
				throw new ParseException();
			} else if (context.currentToken().equals(")")) {
				context.skipToken(")");
				break;
			} else {
				Node expr = new Expr();
				expr.parse(context);
				node = expr;
			}
		}
	}

	public IStockPattern execute() {
		return new NotPattern(node.execute());
	}
}
