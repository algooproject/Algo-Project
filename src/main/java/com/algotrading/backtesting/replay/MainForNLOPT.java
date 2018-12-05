package com.algotrading.backtesting.replay;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.stock.PortfolioHistory;
import com.algotrading.backtesting.strategy.Strategies;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.backtesting.util.WriteToFile;

public class MainForNLOPT {
	static int i = 1;

	public static double execute(int intRSIMagnitude, double dblRSILowerThan, int intSMAMagnitude,
			double dblVolumeHigherThan, int intReentryRSIMagnitude, double dblReentryRSILowerThan, double dblTakeProfit,
			double dblStopLoss) {
		try {
			System.out.println(i + " loop");
			i++;
			// System.out.println("int intRSIMagnitude:" + intRSIMagnitude);
			// System.out.println("double dblRSILowerThan:" + dblRSILowerThan);
			// System.out.println("int intSMAMagnitude:" + intSMAMagnitude);
			// System.out.println("double dblVolumeHigherThan:" +
			// dblVolumeHigherThan);
			// System.out.println("int intReentryRSIMagnitude:" +
			// intReentryRSIMagnitude);
			// System.out.println("double dblReentryRSILowerThan:" +
			// dblReentryRSILowerThan);
			// System.out.println("double dblTakeProfit:" + dblTakeProfit);
			// System.out.println("double dblStopLoss:" + dblStopLoss + "\n");
			// System.out.println(i + "loop");
			// System.out.println("Constants.SRC_MAIN_RESOURCE_FILEPATH: " +
			// Constants.SRC_MAIN_RESOURCE_FILEPATH);
			Date startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2000-02-01");
			Date endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2017-12-27");
			// Date endDate =
			// Constants.DATE_FORMAT_YYYYMMDD.parse("2000-01-10");
			int initialCash = 300000;
			PortfolioHistory history = new PortfolioHistory(initialCash);
			WriteToFile toFile = new WriteToFile();
			toFile.buyStrategies(Constants.SRC_MAIN_RESOURCE_FILEPATH + "buyStrategiesNLOPT.txt", intRSIMagnitude,
					dblRSILowerThan, intSMAMagnitude, dblVolumeHigherThan);
			toFile.reentryStrategies(Constants.SRC_MAIN_RESOURCE_FILEPATH + "reentryStrategiesNLOPT.txt",
					intReentryRSIMagnitude, dblReentryRSILowerThan);
			toFile.sellStrategies(Constants.SRC_MAIN_RESOURCE_FILEPATH + "sellStrategiesNLOPT.txt", dblTakeProfit);
			toFile.exitStrategies(Constants.SRC_MAIN_RESOURCE_FILEPATH + "exitStrategiesNLOPT.txt", dblStopLoss);
			Strategies strategies = new Strategies(Constants.SRC_MAIN_RESOURCE_FILEPATH + "buyStrategiesNLOPT.txt",
					Constants.SRC_MAIN_RESOURCE_FILEPATH + "sellStrategiesNLOPT.txt",
					Constants.SRC_MAIN_RESOURCE_FILEPATH + "exitStrategiesNLOPT.txt",
					Constants.SRC_MAIN_RESOURCE_FILEPATH + "reentryStrategiesNLOPT.txt");
			AvailableStocks availableStocks = new AvailableStocks(Constants.SRC_MAIN_RESOURCE_FILEPATH,
					"availableStocks2.txt");
			TradingDate tradingDate = new TradingDate(Constants.SRC_MAIN_RESOURCE_FILEPATH + "tradingDate.txt");
			Replay replay = new Replay(startDate, endDate, history, strategies, availableStocks, tradingDate,
					initialCash);
			replay.simulate();
			PortfolioHistory portfolioHistory = replay.getPortfolioHistory();
			Date lastTradingDate = tradingDate.rollBackCurrentDate(endDate, endDate);
			double years = ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) / 365);
			double profitRate = portfolioHistory.portfolioReturn(lastTradingDate);
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
			System.out.println("Annual Increment: " + (Math.pow(1 + profitRate, 1 / years) - 1));
			// System.out.println("Total Traded Volume: " +
			// replay.getTotalTradedVolume());
			// System.out.println("Total Transaction Cost: " +
			// replay.getTotalTrasactionCost() + "\n\n");
			return 0 - portfolioHistory.getNetProfit(lastTradingDate);
		} catch (IOException ioe) {
			System.out.println("IOException");
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

	public static void main(String[] args) throws IOException, ParseException {
		System.out.println("Print from Main: " + execute(150, 30, 60, 1.6, 150, 30, 1.15, 0.9));
	}
}
