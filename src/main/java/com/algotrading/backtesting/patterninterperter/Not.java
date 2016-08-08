package com.algotrading.backtesting.patterninterperter;

import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.pattern.NotSignal;

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

	public StockSignal execute() {
		return new NotSignal(node.execute());
	}
}
