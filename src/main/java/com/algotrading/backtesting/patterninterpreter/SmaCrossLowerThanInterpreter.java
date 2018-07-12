package com.algotrading.backtesting.patterninterpreter;

import java.text.ParseException;

import com.algotrading.backtesting.pattern.SmaCrossHigherThanSignal;
import com.algotrading.backtesting.pattern.StockSignal;

public class SmaCrossLowerThanInterpreter implements Node {
	private static String name = "SMACrossLowerThan";
	private int coeff;

	@Override
	public void parse(Context context) throws ParseException {
		while (true) {
			if (context.currentToken() == null) {
				throw new ParseException(name, 0);
			} else if (context.currentToken()
					.equals("SMACrossLowerThan[")) {
				context.skipToken("SMACrossLowerThan[");
			} else if (context.currentToken()
					.equals("]")) {
				context.skipToken("]");
				break;
			} else {
				String keyValue = context.currentToken();
				context.skipToken(context.currentToken());

				String[] keyValuePair = keyValue.split("=");
				String key = keyValuePair[0];
				String value = keyValuePair[1];

				if ("coeff".equals(key)) {
					coeff = Integer.parseInt(value);
				} else {
					throw new ParseException(name + " no field match: " + key, 0);
				}
			}
		}
	}

	@Override
	public StockSignal execute() {
		return new SmaCrossHigherThanSignal(coeff);
	}
}
