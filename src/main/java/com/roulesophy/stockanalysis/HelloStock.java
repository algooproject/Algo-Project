package com.roulesophy.stockanalysis;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.fx.FxQuote;
import yahoofinance.quotes.fx.FxSymbols;

public class HelloStock {

	public static void main(String[] args){
		try {
//			textBookExample();
			selfTest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void selfTest() throws IOException{
		Stock hk1382 = YahooFinance.get("1382.HK", true);
		Calendar from = Calendar.getInstance();
		Calendar to	  = Calendar.getInstance();
		from.add(Calendar.YEAR, -5);
		List<HistoricalQuote> history = hk1382.getHistory(from, to, Interval.DAILY);
		for (HistoricalQuote quote : history) {
			System.out.print(new SimpleDateFormat("yyyyMMdd").format(quote.getDate().getTime()));
			System.out.print("\t");
			System.out.print(quote.getOpen());
			System.out.print("\t");
			System.out.print(quote.getClose());
			System.out.print("\t");
			System.out.print(quote.getHigh());
			System.out.print("\t");
			System.out.print(quote.getLow());
			System.out.print("\t");
			System.out.println(quote.getAdjClose());
		}
	}

	private static void textBookExample() throws IOException {
		singleStock();
		multipleStocksAtOnce();
		fxCode();
		singleStockIncludeHistoricalQuotes();
		singleStockIncludeHistoricQuotesInDateRange();
		multipleStocksIncludingHistoricQuotes();
		alternativesFromHistoricQuotes();
		alternativesForHistoricalQuotesWIthDateRange();
	}

	private static void alternativesForHistoricalQuotesWIthDateRange() throws IOException {
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -1); // from 1 year ago
		 
		Stock google = YahooFinance.get("GOOG");
		List<HistoricalQuote> googleHistQuotes = google.getHistory(from, to, Interval.DAILY);
		// googleHistQuotes is the same as google.getHistory() at this point
		// provide some parameters to the getHistory method to send a new request to Yahoo Finance
	}

	private static void alternativesFromHistoricQuotes() throws IOException {
		Stock google = YahooFinance.get("GOOG");
		List<HistoricalQuote> googleHistQuotes = google.getHistory();
	}

	private static void multipleStocksIncludingHistoricQuotes() throws IOException {
		String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
		// Can also be done with explicit from, to and Interval parameters
		Map<String, Stock> stocks = YahooFinance.get(symbols, true);
		Stock intel = stocks.get("INTC");
		Stock airbus = stocks.get("AIR.PA");
	}

	private static void singleStockIncludeHistoricQuotesInDateRange() throws IOException {
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -5); // from 5 years ago
		 
		Stock google = YahooFinance.get("GOOG", from, to, Interval.WEEKLY);
	}

	private static void singleStockIncludeHistoricalQuotes() throws IOException {
		Stock tesla = YahooFinance.get("TSLA", true);
		System.out.println(tesla.getHistory());
	}

	private static void fxCode() throws IOException {
		FxQuote usdeur = YahooFinance.getFx(FxSymbols.USDEUR);
		FxQuote usdgbp = YahooFinance.getFx("USDGBP=X");
		System.out.println(usdeur);
		System.out.println(usdgbp);
	}

	private static void multipleStocksAtOnce() throws IOException {
		String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
		Map<String, Stock> stocks = YahooFinance.get(symbols); // single request
		Stock intel = stocks.get("INTC");
		Stock airbus = stocks.get("AIR.PA");
	}

	private static void singleStock() throws IOException {
		Stock stock;
		try {
			stock = YahooFinance.get("INTC");
			BigDecimal price = stock.getQuote().getPrice();
			BigDecimal change = stock.getQuote().getChangeInPercent();
			BigDecimal peg = stock.getStats().getPeg();
			BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
			
			stock.print();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		Stock intc = YahooFinance.get("INTC");
		BigDecimal price = intc.getQuote(true).getPrice();
	}
}
