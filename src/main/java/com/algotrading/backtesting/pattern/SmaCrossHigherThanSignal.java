package com.algotrading.backtesting.pattern;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;

public class SmaCrossHigherThanSignal extends SmaCrossSignal {

	public SmaCrossHigherThanSignal(int coeff) {
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
		System.out.println(Constants.DATE_FORMAT_YYYYMMDD.format(currentDate) + ": previousDatePrice < previousSma: "
				+ (previousDatePrice < previousSma));
		System.out.println(Constants.DATE_FORMAT_YYYYMMDD.format(currentDate) + ": currentDatePrice > currentSma: "
				+ (currentDatePrice > currentSma));
		if (previousDatePrice < previousSma && currentDatePrice > currentSma) {
			return true;
		}
		return false;
	}
}
