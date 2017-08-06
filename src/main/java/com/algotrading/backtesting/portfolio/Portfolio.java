package com.algotrading.backtesting.portfolio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;

public class Portfolio {

	private Map<String, PortfolioComponent> portfolioComponents;
	private Date date;
	private final double initialCash;
	private double cash;
	private List<BuySellAmount> transaction;

	public Portfolio(Date date, double cash) {
		this.date = date;
		this.portfolioComponents = new TreeMap<>();
		this.cash = cash;
		this.initialCash = cash;
		this.transaction = new ArrayList<>();
	}

	public List<String> getAllTickerName() {
		return new ArrayList<String>(portfolioComponents.keySet());
	}

	public double getCostByTicker(String ticker) {
		PortfolioComponent portfolioComponent = portfolioComponents.get(ticker);
		return portfolioComponent.getUnitPrice();
	}

	public void setDate(Date date) {
		this.date = date;
		for (Map.Entry<String, PortfolioComponent> portfolioComponent : portfolioComponents.entrySet()) {
			portfolioComponent.getValue()
					.setDate(date);
		}
	}

	public void addCash(double increasedCash) {
		this.cash = this.cash + increasedCash;
	}

	public PortfolioComponent getPortfolioComponent(String tickerName) {
		return portfolioComponents.get(tickerName);
	}

	public boolean containsStock(Stock stock) {
		for (String key : portfolioComponents.keySet()) {
			if (key.equals(stock.getTicker()) && portfolioComponents.get(key)
					.getQuantity() != 0) {
				return true;
			}
		}
		return false;
	}

	public void put(PortfolioComponent portfolioComponent) {
		portfolioComponents.put(getTickerFromPortfolioComponent(portfolioComponent), portfolioComponent);
	}

	public void add(PortfolioComponent newComponent) {
		String tickerName = getTickerFromPortfolioComponent(newComponent);
		PortfolioComponent component = portfolioComponents.get(tickerName);
		if (component != null) {
			component.add(newComponent.getQuantity(), newComponent.getUnitPrice());
			portfolioComponents.put(tickerName, component);
			if (component.getQuantity() == 0) {
				portfolioComponents.remove(component.getStock()
						.getTicker());
			}
		} else {
			portfolioComponents.put(tickerName, newComponent);
		}
	}

	public double marketValue() {
		double stockValue = portfolioComponents.values()
				.stream()
				.mapToDouble(pc -> pc.getQuantity() * (pc.getStock()
						.getHistory()
						.get(date)
						.getClose()))
				.sum();
		// System.out.println(stockValue + " " + cash + " " + initialCash);
		return stockValue + cash;
	}

	public double cost() {
		return portfolioComponents.values()
				.stream()
				.mapToDouble(pc -> pc.getQuantity() * pc.getUnitPrice())
				.sum();
	}

	public double getCash() {
		return cash;
	}

	private String getTickerFromPortfolioComponent(PortfolioComponent portfolioComponent) {
		return portfolioComponent.getStock()
				.getTicker();
	}

	public int getStockQuantity(Stock stock) {
		if (containsStock(stock)) {
			return portfolioComponents.get(stock.getTicker())
					.getQuantity();
		} else {
			return 0;
		}
	}

	public void addTransaction(BuySellAmount buySellAmount) {
		this.transaction.add(buySellAmount);
	}

	@Override
	public String toString() {
		return "" + "Date: " + Constants.DATE_FORMAT_YYYYMMDD.format(date) + ", portfolio: " + portfolioComponents
				+ ", cash: " + cash + ", transaction: " + transaction;
	}

	public double getProfit() {
		return marketValue() - initialCash;
	}

	@Override
	public Portfolio clone() {
		Portfolio portfolio = new Portfolio(date, cash);
		for (String key : portfolioComponents.keySet()) {
			PortfolioComponent portfolioComponent = portfolioComponents.get(key);
			portfolio.put(portfolioComponent.clone(date));
		}
		return portfolio;
	}
}
