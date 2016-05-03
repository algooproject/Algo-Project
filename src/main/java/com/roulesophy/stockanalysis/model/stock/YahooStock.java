package com.roulesophy.stockanalysis.model.stock;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	private String DATE_FORMAT = "yyyyMMdd";
	
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
		List<Double> rsiU = rsiUD(date, true);
		List<Double> rsiD = rsiUD(date, false);
		double rs = (EMA(rsiU, scale).doubleValue())/(EMA(rsiD, scale).doubleValue());
		double rsiRaw = 100/(1 + rs);
		return new BigDecimal(100 - rsiRaw);
	}

	private List<Double> rsiUD(Date date, boolean isU) {
		List<Double> rsiClose = new ArrayList<>();
		boolean ignoreFirst = true;
		String lastKey = "";
		for(String key : stockHistory.keySet()) {
			if(ignoreFirst) {
				ignoreFirst = false;
				lastKey = key;
			} else {
				if(key.compareTo(new SimpleDateFormat(DATE_FORMAT).format(date)) > 0) continue;
				HistoricalQuote todayQuote = stockHistory.get(key);
				HistoricalQuote lastDayQuote = stockHistory.get(lastKey);
				if (todayQuote == null) continue;
				if (isU) {
					if(todayQuote.getClose().compareTo(lastDayQuote.getClose()) > 0) {
						rsiClose.add(todayQuote.getClose().doubleValue() - lastDayQuote.getClose().doubleValue());
					} else {
						rsiClose.add(0.0);
					}
				} else {
					if(lastDayQuote.getClose().compareTo(todayQuote.getClose()) > 0) {
						rsiClose.add(lastDayQuote.getClose().doubleValue() - todayQuote.getClose().doubleValue());
					} else {
						rsiClose.add(0.0);
					}
				}
				lastKey = key;
			}
		}
		return rsiClose;
	}

	public BigDecimal EMA(Date date, int scale) {
		List<Double> historicClose = new ArrayList<>();
		for(String key : stockHistory.keySet()) {
			if(key.compareTo(new SimpleDateFormat(DATE_FORMAT).format(date)) <= 0) {
				HistoricalQuote quote = stockHistory.get(key);
				if (quote != null) {
					historicClose.add(quote.getClose().doubleValue());
				}
			}
		}
		return EMA(historicClose, scale);
	}

	private BigDecimal EMA(List<Double> historicClose, int scale) {
		List<Double> emaHistory = new ArrayList<>();
		double smaRaw = 0;
		double firstSma = 0;
		for (int i = 0; i < scale; i++) {
			smaRaw = smaRaw + historicClose.get(i);
		}
		firstSma = smaRaw/scale;
		emaHistory.add(firstSma);
		
		double multiplicand = 2.0/(scale + 1);
		for(int i = scale; i < historicClose.size(); i++) {
			double price = historicClose.get(i);
			double ema1 = price * multiplicand;
			double ema2 = emaHistory.get(emaHistory.size() - 1) * (1-multiplicand);
			double ema = ema1 + ema2;
			emaHistory.add(ema);
		}
		return new BigDecimal(emaHistory.get(emaHistory.size() - 1));
	}

	public BigDecimal SMA(Date date, int scale) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < scale; ) {
			cal.add(Calendar.DATE, -1);
			if (close(cal.getTime()) == null) {
				cal.add(Calendar.DATE, -1);
			} else {
				sum = sum.add(close(cal.getTime()));
				i++;
			}
		}
		return sum.divide(new BigDecimal(scale), RoundingMode.HALF_UP);
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
