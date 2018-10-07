package com.algotrading.backtesting.util;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.PortfolioHistory;

public abstract class PrintMethod {

	protected Date startDate;
	protected Date endDate;
	protected PortfolioHistory portfolioHistory;
	protected boolean isInitialized = false;
	protected double duration = 0;
	private boolean noRoundingForAll = false;
	private int roundToDecimal = 2;

	public PrintMethod() {
	}

	public abstract void record(Date currentDate, Portfolio portfolio) throws ParseException;

	public abstract void print();

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public PortfolioHistory getPortfolioHistory() {
		return portfolioHistory;
	}

	protected double roundTo(double value, int places) {
		if (noRoundingForAll)
			return value;
		double scale = Math.pow(10, places);
		return Math.floor(value * scale + 0.5) / scale;
	}

	protected double roundTo(double value) {
		return roundTo(value, roundToDecimal);
	}

	public void setNoRoundingForAll(boolean option) {
		noRoundingForAll = option;
	}

	public void setRoundingToDecimal(int decimalPlace) {
		roundToDecimal = decimalPlace;
	}

	public void setDatesAndHistory(Date startDate, Date endDate, PortfolioHistory portfolioHistory)
			throws ParseException {
		if (startDate == null)
			throw new ParseException("Setting Error: startDate must not be null.", 0);
		if (endDate == null)
			throw new ParseException("Setting Error: endDate must not be null.", 0);
		this.startDate = startDate;
		this.endDate = endDate;
		this.portfolioHistory = portfolioHistory;
		isInitialized = true;
		duration = ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) / 365.0);
	}

}
