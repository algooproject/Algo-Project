package com.roulesophy.stockanalysis.model.stock;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import yahoofinance.histquotes.HistoricalQuote;

public class YahooStockEmaCalculator1 extends YahooStockEmaCalculator {


	public YahooStockEmaCalculator1(YahooStock yahooStock) {
		super(yahooStock);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BigDecimal calculate(Date date, int scale) {
		return EMA(date, scale);
	}
	
	public BigDecimal EMA(Date date, int scale) {
		Map<String, HistoricalQuote> stockHistory = yahooStock.getStockHistory();
		List<Double> historicClose = new ArrayList<>();
		for(String key : stockHistory.keySet()) {
			if(key.compareTo(new SimpleDateFormat(YahooStock.DATE_FORMAT).format(date)) <= 0) {
				HistoricalQuote quote = stockHistory.get(key);
				if (quote != null) {
					historicClose.add(quote.getClose().doubleValue());
				}
			}
		}
		return EMA(historicClose, scale);
	}

	@Override
	public BigDecimal EMA(List<Double> historicClose, int scale) {
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

}
