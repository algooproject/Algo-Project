package com.algotrading.backtesting.pattern;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.common.AlgoTradingConstants;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;

public class isExitByClosingDownToSignal extends isExitSignal {

	private ClosingHigherThanSignal signal; // Want to use Not
											// VolumeHigherThanSignal
	private String expectedValueType = AlgoTradingConstants.VARIABLE;
	private String expectedValue = AlgoTradingConstants.HOLDINGPRICE;
	private double multiplier = 1;

	public isExitByClosingDownToSignal(double multiplier) throws ParseException {
		super();
		signal = new ClosingHigherThanSignal(AlgoTradingConstants.VARIABLE, AlgoTradingConstants.HOLDINGPRICE, 1,
				multiplier);
	}

	public String getExpectedValueType() {
		return expectedValueType;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public double getMultiplier() {
		return multiplier;
	}

	@Override
	protected boolean secondSignal(Stock stock, Date date, Portfolio portfolio, double buyCostIfMatch)
			throws ParseException {
		if (!signal.signal(stock, date, portfolio, buyCostIfMatch)) {
			// System.out.println("Exiting: setting " + stock.getTicker() + " to
			// 'Disabled' on " + date.toString());
			stock.setStatus(false);
			return true;
		}
		return false;
	}

	@Override
	protected boolean determine(double value) {
		return true;
	}

}
