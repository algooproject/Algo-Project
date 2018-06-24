package com.algotrading.backtesting.patterninterperter;

import java.text.ParseException;

import com.algotrading.backtesting.common.AlgoTradingConstants;
import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.pattern.isExitByClosingDownToSignal;

public class isExitByClosingDownToInterpreter implements Node {
	private static String name = "isExitByClosingDownTo";
	private String expectedValueType = AlgoTradingConstants.NUMBER;
	private String expectedValue;
	private double multiplier = 1;

	@Override
	public void parse(Context context) throws ParseException {
		while (true) {
			if (context.currentToken() == null) {
				throw new ParseException(name, 0);
			} else if (context.currentToken().equals(AlgoTradingConstants.ISEXITBYCLOSINGDOWNTO)) {
				context.skipToken(AlgoTradingConstants.ISEXITBYCLOSINGDOWNTO);
			} else if (context.currentToken().equals("]")) {
				context.skipToken("]");
				break;
			} else {
				String keyValue = context.currentToken();
				context.skipToken(context.currentToken());

				String[] keyValuePair = keyValue.split("=");
				String key = keyValuePair[0];
				String value = keyValuePair[1];

				if ("multiplier".equals(key)) {
					multiplier = Double.parseDouble(value);
				} else {
					throw new ParseException(name + " no field match", 0);
				}
			}
		}
	}

	@Override
	public StockSignal execute() {
		try {
			return new isExitByClosingDownToSignal(multiplier);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
