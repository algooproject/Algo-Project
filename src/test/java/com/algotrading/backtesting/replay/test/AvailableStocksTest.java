package com.algotrading.backtesting.replay.test;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import com.algotrading.backtesting.replay.AvailableStocks;
import com.algotrading.backtesting.stockread.test.StockReadTest;
import com.algotrading.backtesting.util.Constants;

import static org.junit.Assert.assertEquals;

public class AvailableStocksTest {

	protected static String RESOURCE_PATH_NAME = Constants.SRC_TEST_RESOURCE_FILEPATH + AvailableStocksTest.class.getPackage().getName().replace('.', '/') + "/";

	@Test
	public void testRead() throws IOException, ParseException {
		String inputFile = RESOURCE_PATH_NAME + "availableStocks.txt";
		AvailableStocks availableStocks = new AvailableStocks(inputFile);
		assertEquals(8, availableStocks.get()
				.size());
	}

}
