package com.algotrading.stockanalysis.model.indicatorcalculator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.algotrading.stockanalysis.model.stock.YahooStock;

import yahoofinance.histquotes.HistoricalQuote;

public class YahooStockRsiCalculator1 extends YahooStockRsiCalculator {


	public YahooStockRsiCalculator1(YahooStock yahooStock, YahooStockEmaCalculator emaCalculator) {
		super(yahooStock, emaCalculator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BigDecimal calculate(Date date, int scale) {
		return RSI(date, scale);
	}
	
	public BigDecimal RSI(Date date, int scale) {		
		List<Double> rsiU = rsiUD(date, true);
		List<Double> rsiD = rsiUD(date, false);
		double rs = (emaCalculator.EMA(rsiU, scale).doubleValue())/(emaCalculator.EMA(rsiD, scale).doubleValue());
		double rsiRaw = 100/(1 + rs);
		return new BigDecimal(100 - rsiRaw);
	}

	private List<Double> rsiUD(Date date, boolean isU) {
		Map<String, HistoricalQuote> stockHistory = yahooStock.getStockHistory();
		List<Double> rsiClose = new ArrayList<>();
		boolean ignoreFirst = true;
		String lastKey = "";
		for(String key : stockHistory.keySet()) {
			if(ignoreFirst) {
				ignoreFirst = false;
				lastKey = key;
			} else {
				if(key.compareTo(new SimpleDateFormat(YahooStock.DATE_FORMAT).format(date)) > 0) continue;
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

}
