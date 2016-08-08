package com.algotrading.backtesting.strategy;

import java.util.Date;

import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.portfolio.PortfolioComponent;
import com.algotrading.backtesting.stock.Stock;

public class Strategy {
	private StockSignal pattern;
	private double buyCostIfMatch;

	public Strategy(StockSignal pattern, double buyCostIfMatch) {
		super();
		this.pattern = pattern;
		this.buyCostIfMatch = buyCostIfMatch;
	}

	public boolean shouldPutOrder(Stock stock, Date date) {
		return pattern.signal(stock, date);
	}

	public PortfolioComponent buySellAmount(Stock stock, Date date) {
		double unitPrice = stock.getHistory().get(date).getClose();
		return new PortfolioComponent(stock, (int) (buyCostIfMatch / unitPrice), unitPrice);
	}
}
