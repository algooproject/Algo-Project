package com.algotrading.backtesting.replay;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.algotrading.backtesting.stock.PortfolioHistory;
import com.algotrading.backtesting.strategy.Strategies;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.backtesting.util.Print_Console;
import com.algotrading.backtesting.util.WriteToFile;

public class MainForNLOPT {
	static int i = 1;
	static Date startDate;
	static Date endDate;
	static int initialCash;
	static PortfolioHistory history;
	static WriteToFile toFile = new WriteToFile();
	static AvailableStocks availableStocks;
	static TradingDate tradingDate;
	static PortfolioHistory portfolioHistory;
	static Date lastTradingDate;
	static double years;
	static double profitRate;
	static double ranNo;
	static Replay replay;
	static Strategies strategies;
	static Calendar cal;
	static Date date;
	static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ssSSSS");
	static String formattedDate;
	private static MainForNLOPT instance = null;

	private MainForNLOPT() {
		try {
			ranNo = Math.random();
			startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2000-02-01");
			// System.out.println("init: " + startDate);
			endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-12-27");
			// Date endDate =
			// Constants.DATE_FORMAT_YYYYMMDD.parse("2000-01-10");
			// System.out.println(Constants.SRC_MAIN_RESOURCE_FILEPATH);
			availableStocks = new AvailableStocks(Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH, "availableStocks2.txt");
			// System.out.println(Constants.SRC_MAIN_RESOURCE_FILEPATH);
			tradingDate = new TradingDate(Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH + "tradingDate.txt");
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static MainForNLOPT getInstance() {
		if (instance == null) {
			instance = new MainForNLOPT();
		}
		return instance;
	}

	public static double execute(int intRSIMagnitude, double dblRSILowerThan, int intSMAMagnitude,
			double dblVolumeHigherThan, int intReentryRSIMagnitude, double dblReentryRSILowerThan, double dblTakeProfit,
			double dblStopLoss) {
		try {
			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			System.out.println("Start_Execute " + formattedDate);

			// System.out.println(i + " loop (" + ranNo + ")- ");
			System.out.println(i + " loop");
			i++;
			/*
			 * System.out.println("int intRSIMagnitude:" + intRSIMagnitude);
			 * System.out.println("double dblRSILowerThan:" + dblRSILowerThan);
			 * System.out.println("int intSMAMagnitude:" + intSMAMagnitude);
			 * System.out.println("double dblVolumeHigherThan:" +
			 * dblVolumeHigherThan);
			 * System.out.println("int intReentryRSIMagnitude:" +
			 * intReentryRSIMagnitude);
			 * System.out.println("double dblReentryRSILowerThan:" +
			 * dblReentryRSILowerThan);
			 * System.out.println("double dblTakeProfit:" + dblTakeProfit);
			 * System.out.println("double dblStopLoss:" + dblStopLoss + "\n");
			 * System.out.println(i + "loop");
			 */
			// System.out.println("Constants.SRC_MAIN_RESOURCE_FILEPATH: " +
			// Constants.SRC_MAIN_RESOURCE_FILEPATH);
			initialCash = 300000;
			history = new PortfolioHistory();

			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			// System.out.println("Write File Start Time " + formattedDate);

			toFile.buyStrategies(Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH + "buyStrategiesNLOPT.txt", intRSIMagnitude,
					dblRSILowerThan, intSMAMagnitude, dblVolumeHigherThan, intReentryRSIMagnitude,
					dblReentryRSILowerThan);
			// toFile.reentryStrategies(Constants.SRC_MAIN_RESOURCE_FILEPATH +
			// "reentryStrategiesNLOPT.txt",
			// intReentryRSIMagnitude, dblReentryRSILowerThan);
			toFile.sellStrategies(Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH + "sellStrategiesNLOPT.txt", dblTakeProfit,
					dblStopLoss);
			// toFile.exitStrategies(Constants.SRC_MAIN_RESOURCE_FILEPATH +
			// "exitStrategiesNLOPT.txt", dblStopLoss);

			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			// System.out.println("Write File End Time and " + formattedDate);

			strategies = new Strategies(Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH + "buyStrategiesNLOPT.txt",
					Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH + "sellStrategiesNLOPT.txt");
			// System.out.println("pass to replay");
			// System.out.println("pass to replay: " + startDate.toString());

			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			// System.out.println("Read File End Time " + formattedDate);

			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			System.out.println("Replay_Start_Time " + formattedDate);

			replay = new Replay(startDate, endDate, history, strategies, availableStocks, tradingDate, initialCash,
					new Print_Console());
			replay.simulate();

			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			System.out.println("Replay_End_Time " + formattedDate);

			portfolioHistory = replay.getPortfolioHistory();
			lastTradingDate = tradingDate.rollBackCurrentDate(endDate, endDate);
			years = ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) / 365);
			profitRate = portfolioHistory.portfolioReturn(lastTradingDate);
			// int days = Duration.between(startDate, endDate).toDays();
			// Date firstTradingDate = tradingDate.rollToCurrentDate(startDate,
			// startDate);
			// System.out.println(portfolioHistory);
			// System.out.println("Net Profit: " +
			// portfolioHistory.getNetProfit(lastTradingDate) + "\n");
			// System.out.println("Start: " + startDate.toString() + "; End: " +
			// endDate.toString());
			// System.out.println("Duration: " + years + " years");
			// System.out.println("Profit Rate: " + profitRate);
			// System.out.println("Annual Increment: " + (Math.pow(1 +
			// profitRate, 1 / years) - 1));
			// System.out.println("Total Traded Volume: " +
			// replay.getTotalTradedVolume());
			// System.out.println("Total Transaction Cost: " +
			// replay.getTotalTrasactionCost() + "\n\n");

			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			System.out.println("End_Execute " + formattedDate);

			return 0 - portfolioHistory.getNetProfit(lastTradingDate);
		} catch (IOException ioe) {
			System.out.println("IOException");
			System.out.println(ioe.getMessage());
			System.out.println(i + "loop");
			System.out.println("int intRSIMagnitude:" + intRSIMagnitude);
			System.out.println("double dblRSILowerThan:" + dblRSILowerThan);
			System.out.println("int intSMAMagnitude:" + intSMAMagnitude);
			System.out.println("double dblVolumeHigherThan:" + dblVolumeHigherThan);
			System.out.println("int intReentryRSIMagnitude:" + intReentryRSIMagnitude);
			System.out.println("double dblReentryRSILowerThan:" + dblReentryRSILowerThan);
			System.out.println("double dblTakeProfit:" + dblTakeProfit);
			System.out.println("double dblStopLoss:" + dblStopLoss + "\n");
			// throw ioe;
			return -1;

		} catch (ParseException pe) {
			System.out.println("ParseException");
			System.out.println(pe.getMessage());
			System.out.println(i + "loop");
			System.out.println("int intRSIMagnitude:" + intRSIMagnitude);
			System.out.println("double dblRSILowerThan:" + dblRSILowerThan);
			System.out.println("int intSMAMagnitude:" + intSMAMagnitude);
			System.out.println("double dblVolumeHigherThan:" + dblVolumeHigherThan);
			System.out.println("int intReentryRSIMagnitude:" + intReentryRSIMagnitude);
			System.out.println("double dblReentryRSILowerThan:" + dblReentryRSILowerThan);
			System.out.println("double dblTakeProfit:" + dblTakeProfit);
			System.out.println("double dblStopLoss:" + dblStopLoss + "\n");
			// throw pe;
			return -2;

		} catch (Exception e) {
			System.out.println("Exception");
			System.out.println(e.toString());
			System.out.println(i + "loop");
			System.out.println("int intRSIMagnitude:" + intRSIMagnitude);
			System.out.println("double dblRSILowerThan:" + dblRSILowerThan);
			System.out.println("int intSMAMagnitude:" + intSMAMagnitude);
			System.out.println("double dblVolumeHigherThan:" + dblVolumeHigherThan);
			System.out.println("int intReentryRSIMagnitude:" + intReentryRSIMagnitude);
			System.out.println("double dblReentryRSILowerThan:" + dblReentryRSILowerThan);
			System.out.println("double dblTakeProfit:" + dblTakeProfit);
			System.out.println("double dblStopLoss:" + dblStopLoss + "\n");
			// throw e;
			return -3;

		}
	}

	public static void main(String[] args) throws IOException, ParseException, Exception {
		MainForNLOPT mainForNLOPT = MainForNLOPT.getInstance();
		System.out.println("Print from Main: " + mainForNLOPT.execute(247, 22.72, 33, 1.8, 73, 35, 1.15, 0.9));
	}
}
