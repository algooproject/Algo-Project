package com.algotrading.backtesting.replay;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;

import com.algotrading.backtesting.stock.PortfolioHistory;
import com.algotrading.backtesting.strategy.Strategies;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.backtesting.util.Print_Console;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		LocalTime startTime = LocalTime.now();
		// System.out.println("Constants.SRC_MAIN_RESOURCE_FILEPATH: " +
		// Constants.SRC_MAIN_RESOURCE_FILEPATH);
		TradingDate tradingDate = new TradingDate(Constants.SRC_MAIN_RESOURCE_FILEPATH + "tradingDate.txt");
		Date startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2013-01-02");
		Date endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-12-27");
		endDate = tradingDate.rollBackCurrentDate(endDate, endDate);
		// startDate = tradingDate.rollToCurrentDate(startDate, endDate);
		int initialCash = 300000;
		PortfolioHistory history = new PortfolioHistory();

		Strategies strategies = new Strategies(Constants.SRC_MAIN_RESOURCE_FILEPATH + "buyStrategies1.txt",
				Constants.SRC_MAIN_RESOURCE_FILEPATH + "sellStrategies1.txt");
		LocalTime midTime1 = LocalTime.now();
		System.out.println("RunTime after strategies= " + startTime.until(midTime1, MILLIS));
		AvailableStocks availableStocks = new AvailableStocks(Constants.SRC_MAIN_RESOURCE_FILEPATH,
				"availableStocks1.txt");

		LocalTime midTime2 = LocalTime.now();
		System.out.println("RunTime after availableStocks= " + startTime.until(midTime2, MILLIS));
		// new Print_KPI(Constants.SRC_MAIN_RESOURCE_FILEPATH + "KPI/");
		// new Print_Console();
		Replay replay = new Replay(startDate, endDate, history, strategies, availableStocks, tradingDate, initialCash,
				new Print_Console());
		LocalTime midTime3 = LocalTime.now();
		System.out.println("RunTime after replay construct = " + startTime.until(midTime3, MILLIS));
		replay.setRecordSwitch(false);
		replay.simulate();
		history.put(endDate, replay.getPortfolio());
		// replay.getPrintMethod().setNoRoundingForAll(true);
		replay.print();
		LocalTime endTime = LocalTime.now();
		System.out.println("RunTime = " + startTime.until(endTime, MILLIS));
	}

}
