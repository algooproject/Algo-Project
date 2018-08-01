package com.algotrading.backtesting.util;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;

public class Print_Console extends PrintMethod {

	public Print_Console() {
		super();
	}

	@Override
	public void record(Date currentDate, Portfolio portfolio) throws ParseException {
		if (!isInitialized)
			throw new ParseException("Record Error: Print_Console has not been initialized.", 0);
		portfolioHistory.put(currentDate, portfolio);
		portfolioHistory.addTransactions(portfolio.getTransactions());
	}

	@Override
	public void print() {
		double profitRate = portfolioHistory.portfolioReturn(endDate);

		System.out.println(portfolioHistory);

		System.out.println("Effective Start Date: " + startDate.toString());
		System.out.println("Effective End Date: " + endDate.toString() + "\n");

		System.out.println("Net Profit: " + roundTo(portfolioHistory.getNetProfit(endDate), 2) + "\n");

		System.out.println("Duration: " + duration + " years");
		System.out.println("Profit Rate: " + roundTo(profitRate, 2));
		System.out.println("Annual Increment: " + roundTo((Math.pow(1 + profitRate, 1 / duration) - 1), 2));

		System.out.println("Total Traded Volume: " + portfolioHistory.getTotalTradedVolume());
		System.out.println("Total Transaction Cost: " + portfolioHistory.getTotalTransactionCost());

	}

	@Override
	public void portfolioHistoryInit() {
	}

}
