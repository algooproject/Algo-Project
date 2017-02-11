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

	public int getQuantity() {
		return quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public PortfolioComponent add(int addQuantity, double addUnitPrice) {
		int newQuantity = quantity + addQuantity;
		double newUnitPrice = (cost() + addQuantity * addUnitPrice) / (newQuantity);
		return new PortfolioComponent(stock, newQuantity, newUnitPrice);
	}

	public PortfolioComponent add(PortfolioComponent pc) {
		if (pc.getStock()
				.getTicker()
				.equals(getStock().getTicker())) {
			return add(pc.getQuantity(), pc.getUnitPrice());
		}
		return new PortfolioComponent(stock, quantity, unitPrice);
	}

	@Override
	public String toString() {
		return stock + ":" + unitPrice + "@" + quantity;
	}

	@Override
	public PortfolioComponent clone() {
		return new PortfolioComponent(stock, quantity, unitPrice);
	}
}
