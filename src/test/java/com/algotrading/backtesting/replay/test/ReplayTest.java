package com.algotrading.backtesting.replay.test;

import com.algotrading.backtesting.pattern.MustTrueSignal;
import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.replay.*;
import com.algotrading.backtesting.stock.PortfolioHistory;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;
import com.algotrading.backtesting.strategy.Strategies;
import com.algotrading.backtesting.strategy.Strategy;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.backtesting.util.Print_Console;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReplayTest {

	private Date startDate;
	private Date endDate;
	private Date middleDate;
	private StockSignal mustTrueSignal;
	private Strategy buyStrategy;
	private Strategy sellStrategy;
	private Strategies strategies;
	private TradingDate tradingDate;
	private AvailableStocks availableStocks;
	private DynamicAvailableStocks dynamicAvailableStocks;

	private StockHistory stockHistory1382StartDate;
	private StockHistory stockHistory1382MiddleDate;
	private StockHistory stockHistory1382EndDate;
	private StockHistory stockHistory0281StartDate;
	private StockHistory stockHistory0281MiddleDate;
	private StockHistory stockHistory0281EndDate;

	private Stock stock1382;
	private Stock stock0281;

	public ReplayTest() throws ParseException {
		startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-04-01");
		endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-04-11");
		middleDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-04-06");

		mustTrueSignal = new MustTrueSignal();

		buyStrategy = new Strategy(mustTrueSignal, 100);
		sellStrategy = new Strategy(mustTrueSignal, 100);
		strategies = new Strategies();
		strategies.addBuySignal(buyStrategy);
		strategies.addSellSignal(sellStrategy);

		tradingDate = new TradingDate();
		tradingDate.add(startDate);
		tradingDate.add(middleDate);
		tradingDate.add(endDate);

		stockHistory1382StartDate = new StockHistory(startDate, 5, 5, 5, 5, 5, 1000);
		stockHistory1382MiddleDate = new StockHistory(middleDate, 6, 6, 6, 6, 6, 1000);
		stockHistory1382EndDate = new StockHistory(endDate, 7, 7, 7, 7, 7, 1000);
		Map<Date, StockHistory> stockMap1382 = new HashMap<>();
		stockMap1382.put(startDate, stockHistory1382StartDate);
		stockMap1382.put(middleDate, stockHistory1382MiddleDate);
		stockMap1382.put(endDate, stockHistory1382EndDate);
		stock1382 = new Stock("1382", stockMap1382);

		stockHistory0281StartDate = new StockHistory(startDate, 8, 8, 8, 8, 8, 1000);
		stockHistory0281MiddleDate = new StockHistory(middleDate, 9, 9, 9, 9, 9, 1000);
		stockHistory0281EndDate = new StockHistory(endDate, 10, 10, 10, 10, 10, 1000);

		Map<Date, StockHistory> stockMap0281 = new HashMap<>();
		stockMap0281.put(startDate, stockHistory0281StartDate);
		stockMap0281.put(middleDate, stockHistory0281MiddleDate);
		stockMap0281.put(endDate, stockHistory0281EndDate);
		stock0281 = new Stock("0281", stockMap0281);

		availableStocks = new AvailableStocks();
		availableStocks.add(stock1382);
		availableStocks.add(stock0281);
		dynamicAvailableStocks = new FixedAvailableStocks(availableStocks);
	}

	@Test
	public void test1_test() throws Exception {
		PortfolioHistory history = new PortfolioHistory();
		Replay replay = new Replay(startDate, endDate, history, strategies, dynamicAvailableStocks, tradingDate, 30000,
				new Print_Console());

		replay.simulate();
		PortfolioHistory portfolioHistory = replay.getPortfolioHistory();
		System.out.println(portfolioHistory);
	}

}
