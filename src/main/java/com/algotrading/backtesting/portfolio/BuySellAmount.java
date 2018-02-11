package com.algotrading.backtesting.portfolio;

public class BuySellAmount {

	private PortfolioComponent portfolioComponent;
	private double tradedCash;
	private double transaction;

	public BuySellAmount(PortfolioComponent portfolioComponent, double tradedCash) {
		this.portfolioComponent = portfolioComponent;
		this.tradedCash = tradedCash;
		this.transaction = 0;
	}

	public BuySellAmount(PortfolioComponent portfolioComponent, double tradedCash, double transaction) {
		this.portfolioComponent = portfolioComponent;
		this.tradedCash = tradedCash;
		this.transaction = transaction;
	}
	
	public PortfolioComponent getPortfolioComponent() {
		return portfolioComponent;
	}

	public double getTradedCash() {
		return tradedCash;
	}

	public double getTransaction() {
		return transaction;
	}
	
	@Override
	public String toString() {
		return "portfolioComponent: " + portfolioComponent + ", tradedCash: " + tradedCash;
	}

}
