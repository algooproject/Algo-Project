package com.algotrading.backtesting.pattern.test;

import com.algotrading.backtesting.pattern.ReentrySignal;
import com.algotrading.backtesting.pattern.RsiHigherThanSignal;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.io.StockFileGateway;
import com.algotrading.backtesting.util.Constants;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class ReentrySignalTest {

	private Date date1, date2;
	Portfolio portfolio;
	Stock stockReentry;

	protected static String RESOURCE_PATH_NAME = Constants.SRC_TEST_RESOURCE_FILEPATH
			+ ReentrySignalTest.class.getPackage().getName().replace('.', '/') + "/";

	@Test
	public void test001_reentryTriggered() throws Exception {
		date1 = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-19");
		date2 = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-20");
		portfolio = new Portfolio(date1, 0);
		stockReentry = new Stock("SEHK_Reentry");
		new StockFileGateway(RESOURCE_PATH_NAME).fillTickerData(stockReentry);
		stockReentry.setStatus(false);
		ReentrySignal isht = new ReentrySignal(new RsiHigherThanSignal(10, 0, "number", "50", 1));
		assertTrue(!isht.signal(stockReentry, date1, portfolio, 20000));
		assertTrue(isht.signal(stockReentry, date2, portfolio, 20000));
		isht = new ReentrySignal(new RsiHigherThanSignal(10, 0, "number", "51", 1));
		;
		assertTrue(!isht.signal(stockReentry, date2, portfolio, 20000));
	}

	@Test
	public void test002_reentryNotTriggeredAsStockHasBeenEnabled() throws Exception {
		date1 = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-19");
		date2 = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-20");
		portfolio = new Portfolio(date1, 0);
		stockReentry = new Stock("SEHK_Reentry");
		new StockFileGateway(RESOURCE_PATH_NAME).fillTickerData(stockReentry);
		ReentrySignal isht = new ReentrySignal(new RsiHigherThanSignal(10, 0, "number", "50", 1));
		assertTrue(!isht.signal(stockReentry, date1, portfolio, 20000));
		assertTrue(!isht.signal(stockReentry, date2, portfolio, 20000));
	}

}
