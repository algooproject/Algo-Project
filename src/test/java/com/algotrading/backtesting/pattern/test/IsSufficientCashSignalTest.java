package com.algotrading.backtesting.pattern.test;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import com.algotrading.backtesting.pattern.IsSufficientCashSignal;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IsSufficientCashSignalTest {

	private Date date;
	Portfolio portfolio;
	Stock stock0281;

	@Test
	public void test001_haveSufficientCash() throws ParseException {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 25000);
		stock0281 = new Stock("0281");
		IsSufficientCashSignal iscs = new IsSufficientCashSignal();
		assertTrue(iscs.signal(stock0281, date, portfolio, 20000));
	}

	@Test
	public void test002_notSufficientCash() throws ParseException {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 19000);
		stock0281 = new Stock("0281");
		IsSufficientCashSignal iscs = new IsSufficientCashSignal();
		assertEquals(false, iscs.signal(stock0281, date, portfolio, 20000));
	}

}
