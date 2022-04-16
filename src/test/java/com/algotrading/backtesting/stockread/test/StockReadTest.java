package com.algotrading.backtesting.stockread.test;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.io.StockFileGateway;
import com.algotrading.backtesting.util.Constants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StockReadTest {
	
	protected static String RESOURCE_PATH_NAME = Constants.SRC_TEST_RESOURCE_FILEPATH + StockReadTest.class.getPackage().getName().replace('.', '/') + "/";
	
	@Test
	public void test001_OpenPrice() throws Exception {
		double expected = 98.5;
		String ticker = "SEHK_0001";
		String strDate = "2016-09-30";
		String strInformation = "open";
		assertEquals(expected, getStockHistory(ticker, strDate, strInformation), 0.01);
	}

	@Test
	public void test002_ClosePrice() throws Exception {
		double expected = 100;
		String ticker = "SEHK_0001";
		String strDate = "2016-09-30";
		String strInformation = "close";
		assertEquals(expected, getStockHistory(ticker, strDate, strInformation), 0.01);
	}

	@Test
	public void test003_HighPrice() throws Exception {
		double expected = 100;
		String ticker = "SEHK_0001";
		String strDate = "2016-09-30";
		String strInformation = "high";
		assertEquals(expected, getStockHistory(ticker, strDate, strInformation), 0.01);
	}

	@Test
	public void test004_LowPrice() throws Exception {
		double expected = 98.05;
		String ticker = "SEHK_0001";
		String strDate = "2016-09-30";
		String strInformation = "low";
		assertEquals(expected, getStockHistory(ticker, strDate, strInformation), 0.01);
	}

	@Test
	public void test005_AdjClosePrice() throws Exception {
		double expected = 98.6;
		String ticker = "SEHK_0001";
		String strDate = "2016-09-30";
		String strInformation = "adjclose";
		assertEquals(expected, getStockHistory(ticker, strDate, strInformation), 0.01);
	}

	@Test
	public void test006_Volume() throws Exception {
		double expected = 10000;
		String ticker = "SEHK_0001";
		String strDate = "2016-09-30";
		String strInformation = "volume";
		assertEquals(expected, getStockHistory(ticker, strDate, strInformation), 0.01);
	}

	private double getStockHistory(String ticker, String strDate, String strInformation) throws Exception {
		Stock SEHK_0001_HK = new Stock(ticker);
		new StockFileGateway(RESOURCE_PATH_NAME).fillData(SEHK_0001_HK);
		if (strInformation.equals("open")) {
			return SEHK_0001_HK.getHistory()
					.get(Constants.DATE_FORMAT_YYYYMMDD.parse(strDate))
					.getOpen();
		}
		if (strInformation.equals("close")) {
			return SEHK_0001_HK.getHistory()
					.get(Constants.DATE_FORMAT_YYYYMMDD.parse(strDate))
					.getClose();
		}
		if (strInformation.equals("high")) {
			return SEHK_0001_HK.getHistory()
					.get(Constants.DATE_FORMAT_YYYYMMDD.parse(strDate))
					.getHigh();
		}
		if (strInformation.equals("low")) {
			return SEHK_0001_HK.getHistory()
					.get(Constants.DATE_FORMAT_YYYYMMDD.parse(strDate))
					.getLow();
		}
		if (strInformation.equals("adjclose")) {
			return SEHK_0001_HK.getHistory()
					.get(Constants.DATE_FORMAT_YYYYMMDD.parse(strDate))
					.getAdjClose();
		} else if (strInformation.equals("volume")) {
			return SEHK_0001_HK.getHistory()
					.get(Constants.DATE_FORMAT_YYYYMMDD.parse(strDate))
					.getVolume();
		} else {
			return 0.0;
		}
	}

}
