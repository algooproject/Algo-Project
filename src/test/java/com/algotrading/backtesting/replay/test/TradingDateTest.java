package com.algotrading.backtesting.replay.test;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import com.algotrading.backtesting.replay.TradingDate;
import com.algotrading.backtesting.util.Constants;

public class TradingDateTest {

	protected static String RESOURCE_PATH_NAME = Constants.SRC_TEST_RESOURCE_FILEPATH + TradingDateTest.class.getPackage().getName().replace('.', '/') + "/";
	
	@Test
	public void testDate() throws IOException, ParseException {
		TradingDate tradingDate = new TradingDate(RESOURCE_PATH_NAME + "tradingDate.txt");
		tradingDate.read();
		while (!tradingDate.isNotLastDate()) {
			tradingDate.rollDay();
			System.out.println(tradingDate.currentDate());
		}
	}

}
