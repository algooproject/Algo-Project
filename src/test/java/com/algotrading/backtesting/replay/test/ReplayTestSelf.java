package com.algotrading.backtesting.replay.test;

import com.algotrading.backtesting.pattern.SmaCrossHigherThanSignal;
import com.algotrading.backtesting.pattern.SmaCrossLowerThanSignal;
import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.replay.*;
import com.algotrading.backtesting.stock.PortfolioHistory;
import com.algotrading.backtesting.strategy.Strategies;
import com.algotrading.backtesting.strategy.Strategy;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.backtesting.util.Print_Console;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReplayTestSelf {

	private Date startDate;
	private Date endDate;
	private StockSignal smaCrossHigherThanSignal;
	private StockSignal smaCrossLowerThanSignal;
	private Strategy buyStrategy;
	private Strategy sellStrategy;
	private Strategies strategies;
	private TradingDate tradingDate;
	private AvailableStocks availableStocks;
	private DynamicAvailableStocks dynamicAvailableStocks;
	private double unitBuyCost = 1000000;

	public ReplayTestSelf() throws Exception {
		startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2007-12-31");
		endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-07-13");

		smaCrossHigherThanSignal = new SmaCrossHigherThanSignal(6);
		smaCrossLowerThanSignal = new SmaCrossLowerThanSignal(6);

		// double test = unitBuyCost / 2;
		// double test = 0;
		buyStrategy = new Strategy(smaCrossHigherThanSignal, 100000);
		sellStrategy = new Strategy(smaCrossLowerThanSignal, 100000);
		strategies = new Strategies();
		strategies.addBuySignal(buyStrategy);
		strategies.addSellSignal(sellStrategy);

		Map<String, Integer> lotSizes = new HashMap<>();
		lotSizes.put("2800.HK", 500);

		tradingDate = new TradingDate(Constants.SRC_TEST_RESOURCE_FILEPATH + "smacross/" + "tradingDate.txt");

		availableStocks = new AvailableStocks(Constants.SRC_TEST_RESOURCE_FILEPATH + "smacross/",
				"availableStocks", false);

		availableStocks.get().forEach(stock -> stock.setLotSize(lotSizes.get(stock.getTicker())));
		dynamicAvailableStocks = new FixedAvailableStocks(availableStocks);
	}

	@Test
	public void test1_test() throws ParseException {
		double initialCash = unitBuyCost * availableStocks.get().size();
		PortfolioHistory history = new PortfolioHistory();
		Replay replay = new Replay(startDate, endDate, history, strategies, dynamicAvailableStocks, tradingDate, initialCash,
				new Print_Console());

		replay.simulate();
		PortfolioHistory portfolioHistory = replay.getPortfolioHistory();
		System.out.println(portfolioHistory);
	}

}
