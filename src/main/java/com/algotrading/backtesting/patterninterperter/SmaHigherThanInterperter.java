package com.algotrading.backtesting.patterninterperter;

import java.text.ParseException;

import com.algotrading.backtesting.pattern.SmaHigherThanSignal;
import com.algotrading.backtesting.pattern.StockSignal;

public class SmaHigherThanInterperter implements Node {
	private static String name = "SmaLarger";
	private int magnitude;
	private double expectedValue;

	@Override
	public void parse(Context context) throws ParseException {
		while (true) {
			if (context.currentToken() == null) {
				throw new ParseException(name, 0);
			} else if (context.currentToken()
					.equals("SMAHigher[")) {
				context.skipToken("SMAHigher[");
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

				if ("magnitude".equals(key)) {
					magnitude = Integer.parseInt(value);
				} else if ("expectedValue".equals(key)) {
					expectedValue = Double.parseDouble(value);
				} else {
					throw new ParseException(name + " no field match", 0);
				}
			}
		}
	}

	@Override
	public StockSignal execute() {
		return new SmaHigherThanSignal(magnitude, expectedValue);
	}
}
