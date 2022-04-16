package com.algotrading.backtesting.pattern.test;

import com.algotrading.backtesting.pattern.CashMoreThanSignal;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CashMoreThanSignalTest {

	private Date date;
	private Portfolio portfolio;
	private Stock stock0281;
	private String expectedValueType;
	private String expectedValue;
	private double multiplier = 1;

	@Test
	public void test001_haveSufficientCash() throws Exception {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 25000);
		stock0281 = new Stock("0281");
		expectedValueType = "number";
		expectedValue = "23000";
		CashMoreThanSignal cmt = new CashMoreThanSignal(expectedValueType, expectedValue, multiplier);
		assertTrue(cmt.signal(stock0281, date, portfolio, 20000));
	}

	@Test
	public void test002_notSufficientCash() throws Exception {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 25000);
		stock0281 = new Stock("0281");
		expectedValueType = "number";
		expectedValue = "30000";
		CashMoreThanSignal cmt = new CashMoreThanSignal(expectedValueType, expectedValue, multiplier);
		assertEquals(false, cmt.signal(stock0281, date, portfolio, 20000));
	}

	@Test
	public void test003_mimic_isSufficientCash() throws Exception {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 25000);
		stock0281 = new Stock("0281");
		expectedValueType = "cost";
		expectedValue = "0";
		CashMoreThanSignal cmt = new CashMoreThanSignal(expectedValueType, expectedValue, multiplier);
		assertTrue(cmt.signal(stock0281, date, portfolio, 20000));
	}

	@Test
	public void test004_mimic_NotisSufficientCash() throws Exception {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 19000);
		stock0281 = new Stock("0281");
		expectedValueType = "cost";
		expectedValue = "0";
		CashMoreThanSignal cmt = new CashMoreThanSignal(expectedValueType, expectedValue, multiplier);
		assertEquals(false, cmt.signal(stock0281, date, portfolio, 20000));
	}

	@Test
	public void test005_SufficientCashByMultiplier() throws Exception {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 25000);
		stock0281 = new Stock("0281");
		expectedValueType = "cost";
		expectedValue = "0";
		multiplier = 1.2;
		CashMoreThanSignal cmt = new CashMoreThanSignal(expectedValueType, expectedValue, multiplier);
		assertTrue(cmt.signal(stock0281, date, portfolio, 20000));
	}

	@Test
	public void test006_NotSufficientCashByMultiplier() throws Exception {
		date = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-03-18");
		portfolio = new Portfolio(date, 28000);
		stock0281 = new Stock("0281");
		expectedValueType = "cost";
		expectedValue = "0";
		multiplier = 1.5;
		CashMoreThanSignal cmt = new CashMoreThanSignal(expectedValueType, expectedValue, multiplier);
		assertEquals(false, cmt.signal(stock0281, date, portfolio, 20000));
	}
}
