package com.algotrading.backtesting.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.portfolio.BuySellAmount;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.portfolio.PortfolioComponent;

public class Print_KPI extends PrintMethod {

	private PrintWriter writeKPI;
	private PrintWriter writeTransaction;
	private Map<String, PortfolioComponent> ticker_pc = new TreeMap<>();
	double marketHigh;
	Date marketHighDate;
	double marketLow;
	Date marketLowDate;
	int numOpen = 0;
	int numTakeProfit = 0;
	int numStopLoss = 0;

	boolean recordFirstTime = true;

	public Print_KPI(String outputPath) throws FileNotFoundException, UnsupportedEncodingException {
		super();
		writeKPI = new PrintWriter(outputPath + "KPISummary.txt", "UTF-8");
		writeTransaction = new PrintWriter(outputPath + "Transactions.txt", "UTF-8");
	}

	@Override
	public void record(Date currentDate, Portfolio portfolio) throws ParseException {
		if (!isInitialized)
			throw new ParseException("Record Error: Print_KPI has not been initialized.", 0);
		if (recordFirstTime) {
			marketHigh = portfolioHistory.getInitialValue();
			marketLow = marketHigh;
			marketHighDate = startDate;
			marketLowDate = startDate;

			recordFirstTime = false;
		}
		portfolioHistory.put(currentDate, portfolio);
		if (portfolio.marketValue() > marketHigh) {
			marketHigh = portfolio.marketValue();
			marketHighDate = currentDate;
		}
		if (portfolio.marketValue() < marketLow) {
			marketLow = portfolio.marketValue();
			marketLowDate = currentDate;
		}
		List<BuySellAmount> currentTransactions = portfolio.getTransactions();
		for (int i = 0; i < currentTransactions.size(); i++) {
			PortfolioComponent pc = currentTransactions.get(i).getPortfolioComponent().clone();
			String ticker = pc.getStock().getTicker();
			if (ticker_pc.containsKey(ticker)) {
				double buyPrice = ticker_pc.get(ticker).getUnitPrice();
				double sellPrice = pc.getUnitPrice();
				if (buyPrice < sellPrice) {
					currentTransactions.get(i).setAction("TakeProfit");
					numTakeProfit = numTakeProfit + 1;
				} else {
					currentTransactions.get(i).setAction("StopLoss");
					numStopLoss = numStopLoss + 1;
				}

				ticker_pc.get(ticker).add(pc);
				if (ticker_pc.get(ticker).getQuantity() == 0)
					ticker_pc.remove(ticker);
			} else {
				currentTransactions.get(i).setAction("Open");
				numOpen = numOpen + 1;
				ticker_pc.put(ticker, pc);
			}
		}
		portfolioHistory.addTransactions(currentTransactions);

	}

	private void printKPI() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		writeKPI.println("startDate " + df.format(startDate));
		writeKPI.println("endDate " + df.format(endDate));
		writeKPI.println("Duration " + duration);
		writeKPI.println("initialValue " + roundTo(portfolioHistory.getInitialValue(), 2));
		writeKPI.println("finalValue " + roundTo(portfolioHistory.getMarketValueOnDate(endDate), 2));

		writeKPI.println("marketHigh " + roundTo(marketHigh, 2));
		writeKPI.println("marketHighDate " + df.format(marketHighDate));
		writeKPI.println("marketLow " + roundTo(marketLow, 2));
		writeKPI.println("marketLowDate " + df.format(marketLowDate));

		double netReturn = portfolioHistory.portfolioReturn(endDate);
		writeKPI.println("netProfit " + roundTo(portfolioHistory.getNetProfit(endDate), 2));
		writeKPI.println("netReturn " + roundTo(netReturn, 2));
		writeKPI.println("annualReturn " + roundTo(Math.pow(1 + netReturn, 1 / duration) - 1, 2));

		writeKPI.println("tradedVolume: " + roundTo(portfolioHistory.getTotalTradedVolume(), 2));
		writeKPI.println("transactionCost: " + roundTo(portfolioHistory.getTotalTransactionCost(), 2));

		writeKPI.println("numOpen " + numOpen);
		writeKPI.println("numTakeProfit " + numTakeProfit);
		writeKPI.println("numStopLoss " + numStopLoss);

		writeKPI.close();

	}

	private void printTransactions() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<BuySellAmount> transactions = portfolioHistory.getTransactions();
		for (int i = 0; i < transactions.size(); i++) {
			PortfolioComponent pc = transactions.get(i).getPortfolioComponent();
			String ticker = pc.getStock().getTicker();
			writeTransaction.println(df.format(pc.getDate()) + ";" + ticker + ";" + transactions.get(i).getAction()
					+ ";" + pc.getQuantity() + ";" + roundTo(pc.getUnitPrice(), 2) + ";"
					+ roundTo(pc.getTransactionCost(), 2));
		}
		writeTransaction.close();

	}

	@Override
	public void print() {
		printKPI();
		printTransactions();
		System.out.println("Check the files.");
	}

	@Override
	public void portfolioHistoryInit() {
		if (portfolioHistory.get(startDate) != null) {
			Map<String, PortfolioComponent> portfolioComponents = portfolioHistory.get(startDate)
					.getPortfolioComponents();
			for (String key : portfolioComponents.keySet()) {
				if (portfolioComponents.get(key).getQuantity() != 0) {
					PortfolioComponent pc = portfolioComponents.get(key).clone();
					ticker_pc.put(key, pc);
				}
			}
		}
	}

}
