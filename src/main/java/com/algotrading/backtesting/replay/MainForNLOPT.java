package com.algotrading.backtesting.replay;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

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
	static AvailableStocksWithYearChange availableStocksWithYearChange;
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
	static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	static String formattedDate;
	private static MainForNLOPT instance = null;
	static LinkedList<Double> queue = new LinkedList<Double>();

	private MainForNLOPT() {
		cal = Calendar.getInstance();
		date = cal.getTime();
		formattedDate = dateFormat.format(date);
		System.out.println("start init: " + formattedDate);
		try {
			ranNo = Math.random();
			startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2020-01-01");
			// System.out.println("init: " + startDate);
			endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2020-04-24");
			// Date endDate =
			// Constants.DATE_FORMAT_YYYYMMDD.parse("2000-01-10");
			// System.out.println(Constants.SRC_MAIN_RESOURCE_FILEPATH);
			availableStocks = new AvailableStocks(Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH, "availableStocks5.txt");
			availableStocksWithYearChange = new AvailableStocksWithYearChange(
					Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH, "availablestocksdate.txt");
			// System.out.println(Constants.SRC_MAIN_RESOURCE_FILEPATH);
			tradingDate = new TradingDate(Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH + "tradingDate.txt");
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString() + e.getMessage());
			e.printStackTrace();
		}
		cal = Calendar.getInstance();
		date = cal.getTime();
		formattedDate = dateFormat.format(date);
		System.out.println("finish init: " + formattedDate);
	}

	public static MainForNLOPT getInstance() {
		if (instance == null) {
			instance = new MainForNLOPT();
		}
		return instance;
	}

	static int random(int intMax, int intMin) {
		int intRange = intMax - intMin + 1;
		return (int) (Math.random() * intRange) + intMin;
	}

	public static double execute(int intRSIMagnitude, double dblRSILowerThan, int intSMAMagnitude,
			double dblVolumeHigherThan, int intReentryRSIMagnitude, double dblReentryRSILowerThan, double dblTakeProfit,
			double dblStopLoss) {
		try {
			// System.out.println(i + " loop (" + ranNo + ")");
			System.out.print(i + ",");
			i++;

			// cal = Calendar.getInstance();
			// date = cal.getTime();
			// formattedDate = dateFormat.format(date);
			// System.out.println("xxxxx" + formattedDate + " Start_Execute");

			// System.out.println("int intRSIMagnitude:" + intRSIMagnitude);
			// System.out.println("double dblRSILowerThan:" + dblRSILowerThan);
			// System.out.println("int intSMAMagnitude:" + intSMAMagnitude);
			// System.out.println("double dblVolumeHigherThan:" + dblVolumeHigherThan);
			// System.out.println("int intReentryRSIMagnitude:" + intReentryRSIMagnitude);
			// System.out.println("double dblReentryRSILowerThan:" +
			// dblReentryRSILowerThan);
			// System.out.println("double dblTakeProfit:" + dblTakeProfit);
			// System.out.println("double dblStopLoss:" + dblStopLoss + "\n");
			System.out.print(intRSIMagnitude + "," + dblRSILowerThan + "," + intSMAMagnitude + "," + dblVolumeHigherThan
					+ "," + intReentryRSIMagnitude + "," + dblReentryRSILowerThan + "," + dblTakeProfit + ","
					+ dblStopLoss + ",");
			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			System.out.print(formattedDate + ",");
			// System.out.println("Constants.SRC_MAIN_RESOURCE_FILEPATH: " +
			// Constants.SRC_MAIN_RESOURCE_FILEPATH);
			initialCash = 300000;
			history = new PortfolioHistory();

			// cal = Calendar.getInstance();
			// date = cal.getTime();
			// formattedDate = dateFormat.format(date);
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

			// cal = Calendar.getInstance();
			// date = cal.getTime();
			// formattedDate = dateFormat.format(date);
			// System.out.println("Write File End Time and " + formattedDate);

			strategies = new Strategies(Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH + "buyStrategiesNLOPT.txt",
					Constants.SRC_MAIN_RESOURCE_NLOPT_FILEPATH + "sellStrategiesNLOPT.txt");
			// System.out.println("pass to replay");
			// System.out.println("pass to replay: " + startDate.toString());

			// cal = Calendar.getInstance();
			// date = cal.getTime();
			// formattedDate = dateFormat.format(date);
			// System.out.println("Read File End Time " + formattedDate);

			// System.out.println("xxxxx" + formattedDate + " Replay_Start_Time");
			// System.out.println(tradingDate);
			replay = new Replay(startDate, endDate, history, strategies, availableStocksWithYearChange, tradingDate,
					initialCash, new Print_Console());
			replay.simulate();

			cal = Calendar.getInstance();
			date = cal.getTime();
			formattedDate = dateFormat.format(date);
			System.out.print(formattedDate + ",");
			// System.out.println("xxxxx" + formattedDate + " Replay_End_Time");

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
			// System.out.println("Annual Increment: " + (Math.pow(1 + profitRate, 1 /
			// years) - 1));
			// System.out.println("Total Traded Volume: " + replay.getTotalTradedVolume());
			System.out.println(portfolioHistory.getNetProfit(lastTradingDate) + ","
					+ (Math.pow(1 + profitRate, 1 / years) - 1) + "," + replay.getTotalTradedVolume());
			// System.out.println("Total Transaction Cost: " +
			// replay.getTotalTrasactionCost() + "\n\n");

			// cal = Calendar.getInstance();
			// date = cal.getTime();
			// formattedDate = dateFormat.format(date);
			// System.out.println(formattedDate);
			// System.out.println("xxxxx" + formattedDate + " End_Execute");

			if (queue.size() < 11) {
				queue.offer(Math.pow(1 + profitRate, 1 / years) - 1);
				// queue.offer(1.1);
			} else {
				queue.remove();
				queue.offer(Math.pow(1 + profitRate, 1 / years) - 1);
			}
			// System.out.println(queue.size());
			if (queue.size() == 11) {
				Iterator it = queue.iterator();
				double first = (double) it.next();
				// System.out.println("first: " + first);
				boolean exit = true;
				double other = 0;
				while (it.hasNext()) {
					other = (double) it.next();
					// System.out.println("other: " + other);
					if (Math.abs(first - other) > Math.abs(first / 100)) {
						exit = false;
						break;
					}
				}
				// System.out.println(exit);
				if (exit) {
					// System.out.println("return -3");
					return -3;
				}
			}
			// cal = Calendar.getInstance();
			// date = cal.getTime();
			// formattedDate = dateFormat.format(date);
			// System.out.println(formattedDate);

			return 0 - (Math.pow(1 + profitRate, 1 / years) - 1);
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

		} /*
			 * catch (Exception e) { System.out.println("Exception");
			 * System.out.println(e.toString()); System.out.println(i + "loop");
			 * System.out.println("int intRSIMagnitude:" + intRSIMagnitude);
			 * System.out.println("double dblRSILowerThan:" + dblRSILowerThan);
			 * System.out.println("int intSMAMagnitude:" + intSMAMagnitude);
			 * System.out.println("double dblVolumeHigherThan:" + dblVolumeHigherThan);
			 * System.out.println("int intReentryRSIMagnitude:" + intReentryRSIMagnitude);
			 * System.out.println("double dblReentryRSILowerThan:" +
			 * dblReentryRSILowerThan); System.out.println("double dblTakeProfit:" +
			 * dblTakeProfit); System.out.println("double dblStopLoss:" + dblStopLoss +
			 * "\n"); // throw e; return -3;
			 * 
			 * }
			 */
	}

	public static void main(String[] args) throws IOException, ParseException, Exception {
		Long startTime = System.nanoTime();
		System.out.println(Calendar.getInstance().getTime());
		System.out.println(startTime);
		MainForNLOPT mainForNLOPT = MainForNLOPT.getInstance();
//		System.out.println("Print from Main: "
//				+ mainForNLOPT.execute(random(9, 250), random(2000, 4000) / 100, random(30, 90), random(120, 200) / 100,
//						random(9, 300), random(2000, 5000) / 100, random(105, 135) / 100, random(75, 95) / 100));
		System.out.println("Print from Main: " + mainForNLOPT.execute(14, random(2000, 4000) / 100, random(30, 90),
				random(120, 200) / 100, 14, random(2000, 5000) / 100, random(105, 135) / 100, random(75, 95) / 100));

		Long endTime = System.nanoTime();
		System.out.println(endTime);
		System.out.println(Calendar.getInstance().getTime());
		System.out.println((endTime - startTime) / 1000000);
	}
}