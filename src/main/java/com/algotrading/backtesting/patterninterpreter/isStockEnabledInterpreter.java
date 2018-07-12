package com.algotrading.backtesting.patterninterpreter;

import java.text.ParseException;

import com.algotrading.backtesting.common.AlgoTradingConstants;
import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.pattern.isStockEnabledSignal;

public class isStockEnabledInterpreter implements Node {
	private static String name = "isStockEnabled";

	@Override
	public void parse(Context context) throws ParseException {
		while (true) {
			if (context.currentToken() == null) {
				throw new ParseException(name, 0);
			} else if (context.currentToken().equals(AlgoTradingConstants.ISSTOCKENABLED)) {
				context.skipToken(AlgoTradingConstants.ISSTOCKENABLED);
			} else if (context.currentToken().equals("]")) {
				context.skipToken("]");
				break;
			} else {
				// may need this later
				/*
				 * String keyValue = context.currentToken();
				 * context.skipToken(context.currentToken());
				 * 
				 * String[] keyValuePair = keyValue.split("="); String key =
				 * keyValuePair[0]; String value = keyValuePair[1];
				 */

				throw new ParseException(name + " no field match", 0);
			}
		}
	}

	@Override
	public StockSignal execute() {
		return new isStockEnabledSignal();
	}
}
