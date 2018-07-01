package com.algotrading.backtesting.portfolio;

import java.util.Date;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;

/**
 * This class is date-independent class for component of Portfolio. A Portfolio
 * contains one or more of PortfolioCompoment.
 */
public class PortfolioComponent {

	private Stock stock;
	private int quantity;
	private double unitPrice;
	private double profit = 0;
	private Date date;
	private double transactionCost = 0;
	private String action;

	public PortfolioComponent(Stock stock, int quantity, double unitPrice, Date date) {
		super();
		this.stock = stock;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.date = date;
		this.transactionCost = 0;
	}

	public PortfolioComponent(Stock stock, int quantity, double unitPrice, Date date, double transactionCost) {
		super();
		this.stock = stock;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.date = date;
		this.transactionCost = transactionCost;
	}

	public PortfolioComponent(Stock stock, int quantity, double unitPrice, Date date, double transactionCost,
			String action) {
		super();
		this.stock = stock;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.date = date;
		this.transactionCost = transactionCost;
		this.action = action;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double cost() {
		return quantity * unitPrice;
	}

	public String getAction() {
		return action;
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

	public double getProfit() {
		return profit;
	}

	public double getTransactionCost() {
		return transactionCost;
	}

	public void add(int addQuantity, double addUnitPrice, double transactionCost) {
		this.transactionCost = this.transactionCost + transactionCost;
		if (quantity + addQuantity <= 0) {
			profit = (addUnitPrice - unitPrice) * quantity - this.transactionCost;
			quantity = 0;
			unitPrice = 0;
		} else {
			int newQuantity = quantity + addQuantity;
			double newUnitPrice = newQuantity != 0 ? (cost() + addQuantity * addUnitPrice) / (newQuantity) : 0;
			quantity = newQuantity;
			unitPrice = newUnitPrice;
		}
	}

	public void add(PortfolioComponent pc) {
		if (pc.getStock().getTicker().equals(getStock().getTicker())) {
			add(pc.getQuantity(), pc.getUnitPrice(), pc.getTransactionCost());
		} else {
			throw new RuntimeException("not the same portfolio component");
		}
	}

	@Override
	public String toString() {
		// return stock + ":" + quantity + "@" + unitPrice + ", profit=" +
		// profit;
		return stock + ":" + quantity + "@" + unitPrice + "$" + stock.getHistory().get(date).getClose() + "#"
				+ Constants.DATE_FORMAT_YYYYMMDD.format(date);
	}

	public PortfolioComponent clone(Date date) {
		return new PortfolioComponent(stock, quantity, unitPrice, date, transactionCost);
	}
}
