package com.algotrading.backtesting.signal.test;

import java.text.ParseException;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.algotrading.backtesting.pattern.PositionEqualSignal;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.portfolio.PortfolioComponent;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;

import static org.junit.Assert.assertEquals;

public class PositionEqualSignalTest {

	protected static double BUY_COST_IF_MATCH = 1000;

	protected static double PORTFOLIO_INITIAL_CASH = 0;

	@Test
	public void test001_quanEqual1000() throws Exception {
		// Enter a portfolio with quan 1000, and the signal expects 1000. should
		// return true

		Stock CK = new Stock("SEHK_0001");
		PortfolioComponent position = new PortfolioComponent(CK, 1000, 80, new Date());
		Date date = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-30");
		Portfolio portfolio = new Portfolio(date, 10000);
		portfolio.add(position);
		PositionEqualSignal testSignal = new PositionEqualSignal("number", "1000", 1);
		assertEquals(testSignal.signal(CK, null, portfolio, BUY_COST_IF_MATCH), true);
	}

	@Test
	public void test002_quanNotEqual2000() throws Exception {
		// Enter a portfolio with quan 1000, and the signal expects 2000. should
		// return false

		Stock CK = new Stock("SEHK_0001");
		PortfolioComponent position = new PortfolioComponent(CK, 1000, 80, new Date());
		Date date = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-30");
		Portfolio portfolio = new Portfolio(date, PORTFOLIO_INITIAL_CASH);
		portfolio.add(position);
		PositionEqualSignal testSignal = new PositionEqualSignal("number", "2000", 1);
		assertEquals(testSignal.signal(CK, null, portfolio, BUY_COST_IF_MATCH), false);
	}

	@Test
	public void test003_quanEqual1000Times2() throws Exception {
		// Enter a portfolio with quan 1000, and the signal expects 1000 * 2;
		// multiplier = 2; should return true

		Stock CK = new Stock("SEHK_0001");
		PortfolioComponent position = new PortfolioComponent(CK, 1000, 80, new Date());
		Date date = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-30");
		Portfolio portfolio = new Portfolio(date, PORTFOLIO_INITIAL_CASH);
		portfolio.add(position);
		PositionEqualSignal testSignal = new PositionEqualSignal("number", "2000", 2);
		assertEquals(testSignal.signal(CK, null, portfolio, BUY_COST_IF_MATCH), true);

	}

	@Test
	public void test004_quanNotEqual1000TimesHalf() throws Exception {
		// Enter a portfolio with quan 1000, and the signal expects 1000 * 0.5;
		// multiplier = 0.5; should return false

		Stock CK = new Stock("SEHK_0001");
		PortfolioComponent position = new PortfolioComponent(CK, 1000, 80, new Date());
		Date date = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-30");
		Portfolio portfolio = new Portfolio(date, BUY_COST_IF_MATCH);
		portfolio.add(position);
		PositionEqualSignal testSignal = new PositionEqualSignal("number", "1000", 0.5);
		assertEquals(testSignal.signal(CK, null, portfolio, BUY_COST_IF_MATCH), false);
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void test005_wrongSpelling_number() throws Exception {

		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid ExpectedvalueType -- numbers: no field match");
		Stock CK = new Stock("SEHK_0001");
		PortfolioComponent position = new PortfolioComponent(CK, 1000, 80, new Date());
		Date date = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-30");
		Portfolio portfolio = new Portfolio(date, PORTFOLIO_INITIAL_CASH);
		portfolio.add(position);
		PositionEqualSignal testSignal = new PositionEqualSignal("numbers", "1000", 0.5);
	}

	@Test
	public void test006_wrongSpelling_closing() throws Exception {

		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid Expectedvalue -- closingHistory: no field match");
		Stock CK = new Stock("SEHK_0001");
		PortfolioComponent position = new PortfolioComponent(CK, 1000, 80, new Date());
		Date date = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-30");
		Portfolio portfolio = new Portfolio(date, PORTFOLIO_INITIAL_CASH);
		portfolio.add(position);
		PositionEqualSignal testSignal = new PositionEqualSignal("variable", "closingHistory", 0.5);
	}

	@Test
	public void test007_inputNotNumber() throws Exception {

		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("1000a cannot be converted into double");
		Stock CK = new Stock("SEHK_0001");
		PortfolioComponent position = new PortfolioComponent(CK, 1000, 80, new Date());
		Date date = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-30");
		Portfolio portfolio = new Portfolio(date, PORTFOLIO_INITIAL_CASH);
		portfolio.add(position);
		PositionEqualSignal testSignal = new PositionEqualSignal("number", "1000a", 0.5);
	}

	@Test
	public void test008_closingNotReady() throws Exception {

		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Getting closing price is not implemented yet");
		Stock CK = new Stock("SEHK_0001");
		PortfolioComponent position = new PortfolioComponent(CK, 1000, 80, new Date());
		Date date = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-30");
		Portfolio portfolio = new Portfolio(date, PORTFOLIO_INITIAL_CASH);
		portfolio.add(position);
		PositionEqualSignal testSignal = new PositionEqualSignal("variable", "closing", 0.5);
	}

}
