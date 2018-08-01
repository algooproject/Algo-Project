package com.algotrading.backtesting.util;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.stock.PortfolioHistory;

public class Print {

	private PrintMethod method;

	public Print(String printMethod, Date startDate, Date endDate, PortfolioHistory portfolioHistory)
			throws ParseException, FileNotFoundException, UnsupportedEncodingException {
		if (printMethod.equals("KPI")) {
			method = new Print_KPI(Constants.SRC_MAIN_RESOURCE_FILEPATH);
		} else if (printMethod.equals("Console")) {
			method = new Print_Console();
		} else {
			throw new ParseException("Method '" + printMethod + "' is not defined.", 0);
		}

	}

	public void record(Date currentDate, Portfolio portfolio) throws ParseException {
		method.record(currentDate, portfolio);
	}

	public void print() {
		method.print();
	}

	public PrintMethod getPrintMethod() {
		return method;
	}

}
