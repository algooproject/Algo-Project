package com.algotrading.backtesting.replay.test;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import com.algotrading.backtesting.replay.TradingDate;

public class TradingDateTest {

	@Test
	public void testDate() throws IOException, ParseException {
		TradingDate tradingDate = new TradingDate();
		tradingDate.read();
		while (!tradingDate.isLastDate()) {
			System.out.println(tradingDate.rollDay());
		}
	}

}
