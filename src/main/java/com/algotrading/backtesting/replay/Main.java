package com.algotrading.backtesting.replay;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.stock.PortfolioHistory;
import com.algotrading.backtesting.strategy.Strategies;
import com.algotrading.backtesting.util.Constants;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {

		System.out.println("Constants.SRC_MAIN_RESOURCE_FILEPATH: " + Constants.SRC_MAIN_RESOURCE_FILEPATH);
		Date startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2000-01-04");
		Date endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-12-22");
		int initialCash = 300000;
		PortfolioHistory history = new PortfolioHistory(initialCash);
		Strategies strategies = new Strategies(Constants.SRC_MAIN_RESOURCE_FILEPATH + "buyStrategies1.txt",
				Constants.SRC_MAIN_RESOURCE_FILEPATH + "sellStrategies1.txt");
		AvailableStocks availableStocks = new AvailableStocks(Constants.SRC_MAIN_RESOURCE_FILEPATH,
				"availableStocks1.txt");
		TradingDate tradingDate = new TradingDate(Constants.SRC_MAIN_RESOURCE_FILEPATH + "tradingDate.txt");

//		LotSize lotSize = new LotSize(Constants.SRC_MAIN_RESOURCE_FILEPATH + "lotSize.csv");

		Replay replay = new Replay(startDate, endDate, history, strategies, availableStocks, tradingDate, initialCash);

		replay.simulate();
		PortfolioHistory portfolioHistory = replay.getPortfolioHistory();
		System.out.println(portfolioHistory);
	}

}
