package com.algotrading.backtesting.replay.test;

import com.algotrading.backtesting.replay.AvailableStocks;
import com.algotrading.backtesting.replay.AvailableStocksWithYearChange;
import com.algotrading.backtesting.util.Constants;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DynamicStockListTest {

	protected static String RESOURCE_PATH_NAME = Constants.SRC_TEST_RESOURCE_FILEPATH
			+ DynamicStockListTest.class.getPackage().getName().replace('.', '/') + "/";

	@Test
	public void test001() throws ParseException, IOException {
		AvailableStocksWithYearChange availableStocksWithYearChange = new AvailableStocksWithYearChange(
				RESOURCE_PATH_NAME, "availablestocksdate.txt");
		Date dateB4 = Constants.DATE_FORMAT_YYYYMMDD.parse("2000-08-02");
		AvailableStocks HsiB4 = availableStocksWithYearChange.get(dateB4);
		AvailableStocks expectedHsiB4 = new AvailableStocks(RESOURCE_PATH_NAME, "19991206", true);
		assertEquals(expectedHsiB4.toString(), HsiB4.toString());

		Date onDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2000-08-03");
		AvailableStocks HsiThatDay = availableStocksWithYearChange.get(onDate);
		AvailableStocks expectedHsiThatDay = new AvailableStocks(RESOURCE_PATH_NAME, "20000802", true);
		assertEquals(expectedHsiThatDay.toString(), HsiThatDay.toString());

		Date onAfter = Constants.DATE_FORMAT_YYYYMMDD.parse("2000-08-04");
		AvailableStocks HsiAfter = availableStocksWithYearChange.get(onAfter);
		assertEquals(expectedHsiThatDay.toString(), HsiAfter.toString());
	}

}
