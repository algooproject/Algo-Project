package com.algotrading.backtesting.pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.algotrading.backtesting.stock.Stock;

public abstract class SmaCrossSignal implements StockSignal {

	protected final int coeff;

	public SmaCrossSignal(int coeff) {
		this.coeff = coeff;
	}

	protected Date getPreviousDate(Stock stock, Date date) {
		List<Date> dateList = createDateList(stock);
		int index = dateList.indexOf(date);
		if (index == 0) {
			return null;
		}
		return dateList.get(index - 1);
	}

	protected Double SMA(Stock stock, Date date) {
		List<Date> dateList = createDateList(stock);
		if (dateList.size() < coeff) {
			return null;
		}

		int index = dateList.indexOf(date);
		if (index < coeff - 1) {
			return null;
		}
		return averageClosePrice(stock, dateList, index - coeff + 1, index);
	}

	protected List<Date> createDateList(Stock stock) {
		return new ArrayList<Date>(stock.getHistory()
				.keySet());
	}

	protected double averageClosePrice(Stock stock, List<Date> dateList, int fromIndex, int toIndex) {
		double sum = 0;
		for (int i = fromIndex; i <= toIndex; i++) {
			sum += stock.getHistory()
					.get(dateList.get(i))
					.getClose();
		}
		return sum / (toIndex - fromIndex + 1);
	}

}
