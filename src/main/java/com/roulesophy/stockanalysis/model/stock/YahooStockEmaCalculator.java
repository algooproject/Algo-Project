package com.roulesophy.stockanalysis.model.stock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public abstract class YahooStockEmaCalculator implements IEmaCalculator{
	protected YahooStock yahooStock;
	
	public YahooStockEmaCalculator(YahooStock yahooStock) {
		this.yahooStock = yahooStock;
	}
	
	@Override
	public abstract BigDecimal calculate(Date date, int scale);

	public BigDecimal EMA(List<Double> historicClose, int scale) {
		return null;
	}
	
}
