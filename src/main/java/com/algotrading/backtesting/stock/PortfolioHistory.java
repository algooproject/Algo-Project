package com.algotrading.backtesting.stock;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.portfolio.BuySellAmount;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.util.Constants;

public class PortfolioHistory {

	private Map<Date, Portfolio> history;
	private List<BuySellAmount> transactions = new ArrayList<>();
	private double initValue = 0;
	private boolean initialized = false;

	public PortfolioHistory() {
		this.history = new TreeMap<>();
	}

	public void put(Date date, Portfolio portfolio) {
		history.put(date, portfolio);
	}

	public double portfolioReturn(Date endDate) {
		return getNetProfit(endDate) / initValue;
	}

	public double getMarketValueOnDate(Date date) {
		Portfolio endPortfolio = history.get(date);
		if (endPortfolio == null) {
			return 0;
		} else {
			return endPortfolio.marketValue();
		}
	}

	public double portfolioReturn(Date startDate, Date endDate) throws ParseException {
		Portfolio initPortfolio = history.get(startDate);
		double startDateMarketValue;
		if (initPortfolio == null) {
			throw new ParseException("No portfolio record on start date in history.", 0);
		} else {
			startDateMarketValue = initPortfolio.marketValue();
		}
		return (getMarketValueOnDate(endDate) - startDateMarketValue) / startDateMarketValue;
	}

	public double getNetProfit(Date endDate) {
		if (!initialized)
			System.out.println("Warning: History has not been initialized. Net Profit may be invalid.");
		Portfolio endPortfolio = history.get(endDate);
		if (endPortfolio == null) {
			return 0 - initValue;
		} else {
			return endPortfolio.marketValue() - initValue;
		}
	}

	public double getNetProfit(Date startDate, Date endDate) throws ParseException {
		Portfolio initPortfolio = history.get(startDate);
		double startDateMarketValue;
		if (initPortfolio == null) {
			throw new ParseException("No portfolio record on start date in history.", 0);
		} else {
			startDateMarketValue = initPortfolio.marketValue();
		}

		Portfolio endPortfolio = history.get(endDate);
		double endDateMarketValue;
		if (endPortfolio == null) {
			endDateMarketValue = 0;
		} else {
			endDateMarketValue = endPortfolio.marketValue();
		}
		return endDateMarketValue - startDateMarketValue;
	}

	public void setInitValue(double value) {
		initialized = true;
		initValue = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Date, Portfolio> entry : history.entrySet()) {
			sb.append(Constants.DATE_FORMAT_YYYYMMDD.format(entry.getKey())).append(":").append(entry.getValue())
					.append(", profit: ").append(entry.getValue().marketValue() - initValue).append("\n");
		}
		return sb.toString();
	}

	public Portfolio get(Date date) {
		return history.get(date);
	}

	public double getInitialValue() {
		return initValue;
	}

	public void addTransactions(List<BuySellAmount> transactions) {
		this.transactions.addAll(transactions);
	}

	public List<BuySellAmount> getTransactions() {
		return transactions;
	}

	public double getTotalTradedVolume() {
		double total = 0;
		for (int i = 0; i < transactions.size(); i++)
			total = total + transactions.get(i).getPortfolioComponent().getVolume();
		return total;
	}

	public double getTotalTransactionCost() {
		double total = 0;
		for (int i = 0; i < transactions.size(); i++)
			total = total + transactions.get(i).getPortfolioComponent().getTransactionCost();
		return total;
	}

}
