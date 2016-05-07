package com.roulesophy.stockanalysis.model.stock;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class YahooStock implements IStock {

	private Stock stock;
	private static YahooStockPool stockPool = new YahooStockPool();
	private Map<String, HistoricalQuote> stockHistory;
	public static String DATE_FORMAT = "yyyyMMdd";
	
	public YahooStock(String ticker) {
		if (stockPool.stockExist(ticker)) {
			stock = stockPool.getStock(ticker);
		} else {
			try {
				stock = YahooFinance.get(ticker, true);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.YEAR, -100);
				List<HistoricalQuote> historticalQuoteList = stock.getHistory(cal, Interval.DAILY);
				stockHistory = new TreeMap<String, HistoricalQuote>();
				for(HistoricalQuote quote : historticalQuoteList) {
					stockHistory.put(new SimpleDateFormat(DATE_FORMAT).format(quote.getDate().getTime()), quote);
				}
				
				stockPool.put(ticker, stock, stockHistory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map<String, HistoricalQuote> getStockHistory() {
		return stockHistory;
	}

	public BigDecimal high(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getHigh() : null;
	}

	public BigDecimal open(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getOpen() : null;
	}

	public BigDecimal low(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getLow() : null;
	}

	public BigDecimal close(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getClose() : null;
	}
	
	public BigDecimal adjClose(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getAdjClose() : null;
	}

	private HistoricalQuote getHistoricalPrice(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		HistoricalQuote historicalQuote = stockHistory.get(new SimpleDateFormat(DATE_FORMAT).format(cal.getTime()));
		return historicalQuote;
	}

	public BigDecimal RSI(Date date, int scale) {	
		YahooStockEmaCalculator emaCalculator = new YahooStockEmaCalculator1(this);
		return new YahooStockRsiCalculator1(this, emaCalculator).calculate(date, scale);
	}

	public BigDecimal EMA(Date date, int scale) {
		return new YahooStockEmaCalculator1(this).calculate(date, scale);
	}

	public BigDecimal SMA(Date date, int scale) {
		return new YahooStockSmaCalculator1(this).calculate(date, scale);
	}

	public String currency() {
		return stock.getCurrency();
	}

	public String name() {
		return stock.getName();
	}

	public String ticker() {
		return stock.getSymbol();
	}

	public String stockExchange() {
		return stock.getStockExchange();
	}

	public BigDecimal currentPrice() {
		return stock.getQuote().getPrice();
	}

	public BigDecimal yearLow() {
		return stock.getQuote().getYearLow();
	}

	public BigDecimal yearHigh() {
		return stock.getQuote().getYearHigh();
	}

	public BigDecimal volume() {
		return new BigDecimal(stock.getQuote().getVolume());
	}

	public BigDecimal averageVolume() {
		return new BigDecimal(stock.getQuote().getAvgVolume());
	}

	public BigDecimal eps() {
		return stock.getStats().getEps();
	}

	public BigDecimal pe() {
		return stock.getStats().getPe();
	}

	public BigDecimal peg() {
		return stock.getStats().getPeg();
	}

	public BigDecimal pb() {
		return stock.getStats().getPriceBook();
	}

	public BigDecimal psales() {
		return stock.getStats().getPriceSales();
	}

	public BigDecimal bvps() {
		return stock.getStats().getBookValuePerShare();
	}

	public BigDecimal shortRatio() {
		return stock.getStats().getShortRatio();
	}

	public Date payableDate() {
		Calendar payDate = stock.getDividend().getPayDate();
		return payDate != null ? payDate.getTime() : null;
	}

	public Date exDate() {
		Calendar exDate = stock.getDividend().getExDate();
		return exDate != null ? exDate.getTime() : null;
	}

	public BigDecimal dividendYield() {
		return stock.getDividend().getAnnualYield();
	}

	public BigDecimal dividendYieldPercentage() {
		return stock.getDividend().getAnnualYieldPercent();
	}

}
