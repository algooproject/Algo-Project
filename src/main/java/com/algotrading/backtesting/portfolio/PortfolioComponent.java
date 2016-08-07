package com.algotrading.backtesting.portfolio;

import com.algotrading.backtesting.stock.Stock;

/**
 * This class is date-independent class for component of Portfolio. A Portfolio
 * contains one or more of PortfolioCompoment.
 */
public class PortfolioComponent {

	private Stock stock;
	private int quantity;
	private double unitPrice;

	public PortfolioComponent(Stock stock, int quantity, double unitPrice) {
		super();
		this.stock = stock;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	public double cost() {
		return quantity * unitPrice;
	}

	public Stock getStock() {
		return stock;
	}

}
