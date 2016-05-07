package com.roulesophy.stockanalysis.model.indicatorcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import com.roulesophy.stockanalysis.model.stock.YahooStock;

public class YahooStockSmaCalculator1 extends YahooStockEmaCalculator {


	public YahooStockSmaCalculator1(YahooStock yahooStock) {
		super(yahooStock);
	}

	@Override
	public BigDecimal calculate(Date date, int scale) {
		return SMA(date, scale);
	}
	
	public BigDecimal SMA(Date date, int scale) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < scale; ) {
			cal.add(Calendar.DATE, -1);
			if (yahooStock.close(cal.getTime()) == null) {
				cal.add(Calendar.DATE, -1);
			} else {
				sum = sum.add(yahooStock.close(cal.getTime()));
				i++;
			}
		}
		return sum.divide(new BigDecimal(scale), RoundingMode.HALF_UP);
	}

}
