package com.algotrading.backtesting.pattern.test;

import com.algotrading.backtesting.pattern.isStockEnabledSignal;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class isStockEnabledSignalTest {

	private Date date;
	Portfolio portfolio;
	Stock stock0281;

	@Test
	public void test001_stockEnabled() throws Exception {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 25000);
		stock0281 = new Stock("0281");
		isStockEnabledSignal ise = new isStockEnabledSignal();
		assertTrue(ise.signal(stock0281, date, portfolio, 20000));
	}

	@Test
	public void test002_notSufficientCash() throws Exception {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 19000);
		stock0281 = new Stock("0281");
		stock0281.setStatus(false);
		isStockEnabledSignal ise = new isStockEnabledSignal();
		assertEquals(false, ise.signal(stock0281, date, portfolio, 20000));
	}

}
