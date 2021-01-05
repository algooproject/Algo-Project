package com.algotrading.backtesting.printmethod.test;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.portfolio.PortfolioComponent;
import com.algotrading.backtesting.replay.*;
import com.algotrading.backtesting.stock.PortfolioHistory;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.strategy.Strategies;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.backtesting.util.Print_KPI;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Print_KPITest {

	protected static String RESOURCE_PATH_NAME = Constants.SRC_TEST_RESOURCE_FILEPATH
			+ Print_KPITest.class.getPackage().getName().replace('.', '/') + "/";

	protected static List<String> readFileToArray(String path) throws IOException {
		List<String> lines = new ArrayList<>();

		BufferedReader br = new BufferedReader(new FileReader(path));
		String strLine = "";
		while ((strLine = br.readLine()) != null) {
			lines.add(strLine);
		}
		br.close();
		return lines;
	}

	protected static boolean containsString(List<String> strList, String value) {
		for (int i = 0; i < strList.size(); i++)
			if (strList.get(i).equals(value))
				return true;
		return false;
	}

	@Test
	public void test001_buyConditionsMet() throws ParseException, IOException {
		Date startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-09-28");
		Date endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-19");
		PortfolioHistory history = new PortfolioHistory();
		Strategies strategies = new Strategies(RESOURCE_PATH_NAME + "buyStrategies1.txt",
				RESOURCE_PATH_NAME + "sellStrategies1.txt");
		FixedAvailableStocks availableStocks = new FixedAvailableStocks(RESOURCE_PATH_NAME, "availableStocks1.txt");
		availableStocks.customizedStockFilePath();
		TradingDate tradingDate = new TradingDate(RESOURCE_PATH_NAME + "tradingDate.txt");
		Replay replay = new Replay(startDate, endDate, history, strategies, availableStocks, tradingDate, 300000,
				new Print_KPI(RESOURCE_PATH_NAME + "KPI1/"));
		replay.simulate();
		replay.print();
		List<String> KPILines = readFileToArray(RESOURCE_PATH_NAME + "KPI1/KPISummary.txt");

		assert (containsString(KPILines, "startDate 2016-09-28"));
		assert (containsString(KPILines, "endDate 2016-10-19"));
		assert (containsString(KPILines, "initialValue 300000.0"));
		assert (containsString(KPILines, "finalValue 306000.0"));
		assert (containsString(KPILines, "marketHigh 306000.0"));
		assert (containsString(KPILines, "marketHighDate 2016-09-30"));
		assert (containsString(KPILines, "marketLow 300000.0"));
		assert (containsString(KPILines, "marketLowDate 2016-09-28"));
		assert (containsString(KPILines, "netProfit 6000.0"));
		assert (containsString(KPILines, "netReturn 0.02"));
		assert (containsString(KPILines, "annualReturn 0.41"));
		assert (containsString(KPILines, "tradedVolume: 24000.0"));
		assert (containsString(KPILines, "transactionCost: 0.0"));

		assert (containsString(KPILines, "numOpen 1"));
		assert (containsString(KPILines, "numTakeProfit 1"));
		assert (containsString(KPILines, "numStopLoss 0"));

		List<String> TransactionsLines = readFileToArray(RESOURCE_PATH_NAME + "KPI1/Transactions.txt");
		assert (containsString(TransactionsLines, "2016-09-29;SEHK_TC0001;Open;3000;3.0;0.0"));
		assert (containsString(TransactionsLines, "2016-09-30;SEHK_TC0001;TakeProfit;-3000;5.0;0.0"));
	}

	@Test
	public void test002_stopLoss() throws ParseException, IOException {
		LotSize lotSize = new LotSize(RESOURCE_PATH_NAME + "lotSize.csv");
		Date startDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-03");
		Date endDate = Constants.DATE_FORMAT_YYYYMMDD.parse("2016-10-19");
		PortfolioHistory history = new PortfolioHistory();
		Portfolio portfolio = new Portfolio(startDate, 0);
		Stock stockTC0002 = new Stock("SEHK_TC0002", lotSize.getLotSize("SEHK_TC0002"));
		stockTC0002.read(RESOURCE_PATH_NAME);
		PortfolioComponent TC0002pc = new PortfolioComponent(stockTC0002, 3333, 3, startDate);
		portfolio.put(TC0002pc);
		history.put(startDate, portfolio);
		Strategies strategies = new Strategies(RESOURCE_PATH_NAME + "buyStrategies1.txt",
				RESOURCE_PATH_NAME + "sellStrategies1.txt");
		FixedAvailableStocks availableStocks = new FixedAvailableStocks(RESOURCE_PATH_NAME, "availableStocks2.txt");
		availableStocks.customizedStockFilePath();
		TradingDate tradingDate = new TradingDate(RESOURCE_PATH_NAME + "tradingDate.txt");
		Replay replay = new Replay(startDate, endDate, history, strategies, availableStocks, tradingDate, 0,
				new Print_KPI(RESOURCE_PATH_NAME + "KPI2/"));
		replay.simulate();

		replay.print();
		List<String> KPILines = readFileToArray(RESOURCE_PATH_NAME + "KPI2/KPISummary.txt");

		assert (containsString(KPILines, "startDate 2016-10-03"));
		assert (containsString(KPILines, "endDate 2016-10-19"));
		assert (containsString(KPILines, "initialValue 9665.7"));
		assert (containsString(KPILines, "finalValue 8999.1"));
		assert (containsString(KPILines, "marketHigh 10665.6"));
		assert (containsString(KPILines, "marketHighDate 2016-10-10"));
		assert (containsString(KPILines, "marketLow 8999.1"));
		assert (containsString(KPILines, "marketLowDate 2016-10-14"));
		assert (containsString(KPILines, "netProfit -666.6"));
		assert (containsString(KPILines, "netReturn -0.07"));
		assert (containsString(KPILines, "annualReturn -0.8"));
		assert (containsString(KPILines, "tradedVolume: 8999.1"));
		assert (containsString(KPILines, "transactionCost: 0.0"));

		assert (containsString(KPILines, "numOpen 0"));
		assert (containsString(KPILines, "numTakeProfit 0"));
		assert (containsString(KPILines, "numStopLoss 1"));

		List<String> TransactionsLines = readFileToArray(RESOURCE_PATH_NAME + "KPI2/Transactions.txt");
		assert (containsString(TransactionsLines, "2016-10-14;SEHK_TC0002;StopLoss;-3333;2.7;0.0"));

	}

}
