package com.algotrading.backtesting.pattern.test;

import com.algotrading.backtesting.pattern.ClosingHigherThanSignal;
import com.algotrading.backtesting.pattern.ExitSignal;
import com.algotrading.backtesting.pattern.NotSignal;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.portfolio.PortfolioComponent;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.io.StockFileGateway;
import com.algotrading.backtesting.util.Constants;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class ExitSignalTest {

	private Date date1, date2;
	Portfolio portfolio1, portfolio2;
	Stock stockExit;

	protected static String RESOURCE_PATH_NAME = Constants.SRC_TEST_RESOURCE_FILEPATH
			+ ExitSignalTest.class.getPackage().getName().replace('.', '/') + "/";

	@Test
	public void test001_ExitTriggered() throws ParseException {
		date1 = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-13");
		date2 = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-14");
		portfolio1 = new Portfolio(date1, 0);
		portfolio2 = new Portfolio(date2, 0);

		stockExit = new Stock("SEHK_Exit");
		new StockFileGateway(RESOURCE_PATH_NAME).fillData(stockExit);

		portfolio1.put(new PortfolioComponent(stockExit, 1000, 3, date1));
		portfolio2.put(new PortfolioComponent(stockExit, 1000, 3, date2));

		ExitSignal iebcdt = new ExitSignal(
				new NotSignal(new ClosingHigherThanSignal("variable", "holdingprice", 1, 0.9)));
		assertTrue(!iebcdt.signal(stockExit, date1, portfolio1, 20000));
		assertTrue(iebcdt.signal(stockExit, date2, portfolio2, 20000));
		iebcdt = new ExitSignal(new NotSignal(new ClosingHigherThanSignal("variable", "holdingprice", 1, 0.8)));
		assertTrue(!iebcdt.signal(stockExit, date2, portfolio2, 20000));
	}

	@Test
	public void test002_ExitNotTriggeredAsStockHasBeenDisabled() throws ParseException {
		date1 = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-13");
		date2 = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-14");
		portfolio1 = new Portfolio(date1, 0);
		portfolio2 = new Portfolio(date2, 0);

		stockExit = new Stock("SEHK_Exit");
		new StockFileGateway(RESOURCE_PATH_NAME).fillData(stockExit);
		stockExit.setStatus(false);

		portfolio1.put(new PortfolioComponent(stockExit, 1000, 3, date1));
		portfolio2.put(new PortfolioComponent(stockExit, 1000, 3, date2));

		ExitSignal iebcdt = new ExitSignal(
				new NotSignal(new ClosingHigherThanSignal("variable", "holdingprice", 1, 0.9)));
		assertTrue(!iebcdt.signal(stockExit, date1, portfolio1, 20000));
		assertTrue(!iebcdt.signal(stockExit, date2, portfolio2, 20000));
		iebcdt = new ExitSignal(new NotSignal(new ClosingHigherThanSignal("variable", "holdingprice", 1, 0.8)));
		assertTrue(!iebcdt.signal(stockExit, date2, portfolio2, 20000));
	}
}
