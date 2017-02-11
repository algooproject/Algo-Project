package com.algotrading.backtesting.portfolio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.stock.Stock;

public class Portfolio {

	private Map<String, PortfolioComponent> portfolioComponents;
	private Date date;

	public Portfolio(Date date) {
		this.date = date;
		portfolioComponents = new TreeMap<>();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean containsStock(Stock stock) {
		for (String key : portfolioComponents.keySet()) {
			if (key.equals(stock.getTicker())) {
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
			portfolioComponents.put(tickerName, newComponent);
		} else {
			portfolioComponents.put(tickerName, newComponent);
		}
	}

	public double marketValue() {
		return portfolioComponents.values()
				.stream()
				.mapToDouble(pc -> pc.getQuantity() * pc.getStock()
						.getHistory()
						.get(date)
						.getClose())
				.sum();
	}

	public double cost() {
		return portfolioComponents.values()
				.stream()
				.mapToDouble(pc -> pc.getQuantity() * pc.getUnitPrice())
				.sum();
	}

	private String getTickerFromPortfolioComponent(PortfolioComponent portfolioComponent) {
		return portfolioComponent.getStock()
				.getTicker();
	}

	@Override
	public String toString() {
		return "" + "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + ", portfolio: " + portfolioComponents;
	}

	@Override
	public Portfolio clone() {
		Portfolio portfolio = new Portfolio(date);
		for (String key : portfolioComponents.keySet()) {
			PortfolioComponent portfolioComponent = portfolioComponents.get(key);
			portfolio.put(portfolioComponent.clone());
		}
		return portfolio;
	}
}
