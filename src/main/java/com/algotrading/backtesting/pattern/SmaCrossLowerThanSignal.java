package com.algotrading.backtesting.pattern;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;

public class SmaCrossLowerThanSignal extends SmaCrossSignal {

	public SmaCrossLowerThanSignal(int coeff) {
		super(coeff);
	}

	@Override
	public boolean signal(Stock stock, Date currentDate, Portfolio portfolio, double buyCostIfMatch)
			throws ParseException {
		Date previousDate = getPreviousDate(stock, currentDate);
		Double previousSma = SMA(stock, previousDate);
		Double currentSma = SMA(stock, currentDate);
		if (previousSma == null || currentSma == null) {
			return false;
		}
		Double previousDatePrice = stock.getHistory()
				.get(previousDate)
				.getClose();
		Double currentDatePrice = stock.getHistory()
				.get(previousDate)
				.getClose();
		if (previousDatePrice > previousSma && currentDatePrice < currentSma && portfolio.containsStock(stock)
				&& portfolio.getPortfolioComponent(stock.getTicker())
						.getUnitPrice() < currentDatePrice) {
			return true;
		}
		return false;
	}
}
