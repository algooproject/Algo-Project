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
	private double cash;
	private List<BuySellAmount> transactions;
	private Map<String, PortfolioComponent> ticker_pc = new TreeMap<>();

	public Portfolio(Date date, double cash, Map<String, PortfolioComponent> portfolioComponents) {
		this.date = date;
		this.portfolioComponents = portfolioComponents;
		this.cash = cash;
		this.transactions = new ArrayList<>();
	}

	public Portfolio(Date date, double cash) {
		this.date = date;
		this.portfolioComponents = new TreeMap<>();
		this.cash = cash;
		this.transactions = new ArrayList<>();
	}

	public void setTransaction(List<BuySellAmount> transaction) {
		this.transactions = transaction;
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
			portfolioComponent.getValue().setDate(date);
		}
	}

	public void addCash(double increasedCash) {
		this.cash = this.cash + increasedCash;
	}

	public Map<String, PortfolioComponent> getPortfolioComponents() {
		return portfolioComponents;
	}

	public PortfolioComponent getPortfolioComponent(String tickerName) {
		return portfolioComponents.get(tickerName);
	}

	public boolean containsStock(Stock stock) {
		for (String key : portfolioComponents.keySet()) {
			if (key.equals(stock.getTicker()) && portfolioComponents.get(key).getQuantity() != 0) {
				return true;
			}
		}
		return false;
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

	public void put(PortfolioComponent portfolioComponent) {
		portfolioComponents.put(getTickerFromPortfolioComponent(portfolioComponent), portfolioComponent);
	}

	public void add(PortfolioComponent newComponent) {
		String tickerName = getTickerFromPortfolioComponent(newComponent);
		PortfolioComponent component = portfolioComponents.get(tickerName);
		if (component != null) {
			component.add(newComponent.getQuantity(), newComponent.getUnitPrice(), newComponent.getTransactionCost()
			// Transaction.getTranscationCost(newComponent.getStock(),
			// newComponent.getQuantity() * newComponent.getUnitPrice())
			);
			portfolioComponents.put(tickerName, component);
			if (component.getQuantity() == 0) {
				portfolioComponents.remove(component.getStock().getTicker());
			}
		} else {
			portfolioComponents.put(tickerName, newComponent);
		}
	}

	public double marketValue() {
		double stockValue = portfolioComponents.values().stream()
				.mapToDouble(pc -> pc.getQuantity() * (pc.getStock().getHistory().get(date).getClose())).sum();
		return stockValue + cash;
	}

	public double cost() {
		return portfolioComponents.values().stream().mapToDouble(pc -> pc.getQuantity() * pc.getUnitPrice()).sum();
	}

	public double getCash() {
		return cash;
	}

	private String getTickerFromPortfolioComponent(PortfolioComponent portfolioComponent) {
		return portfolioComponent.getStock().getTicker();
	}

	public int getStockQuantity(Stock stock) {
		if (containsStock(stock)) {
			return portfolioComponents.get(stock.getTicker()).getQuantity();
		} else {
			return 0;
		}
	}

	public void addTransaction(BuySellAmount buySellAmount) {
		this.transactions.add(buySellAmount);
	}

	public List<BuySellAmount> getTransactions() {
		return transactions;
	}

	public Map<String, PortfolioComponent> getTicker_pc() {
		return ticker_pc;
	}

	public void setTicker_pc(Map<String, PortfolioComponent> ticker_pc) {
		this.ticker_pc = ticker_pc;
	}

	@Override
	public String toString() {
		return "" + "Date: " + Constants.DATE_FORMAT_YYYYMMDD.format(date) + ", portfolio: " + portfolioComponents
				+ ", cash: " + cash + ", transaction: " + transactions;
	}

	@Override
	public Portfolio clone() {
		Portfolio portfolio = new Portfolio(date, cash);
		for (String key : portfolioComponents.keySet()) {
			PortfolioComponent portfolioComponent = portfolioComponents.get(key);
			portfolio.put(portfolioComponent.clone(date));
		}
		portfolio.setTicker_pc(ticker_pc);
		return portfolio;
	}
}
