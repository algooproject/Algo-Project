package com.algotrading.backtesting.stock;

import java.math.BigDecimal;
import java.util.Date;

public interface IStock {

	public BigDecimal high(Date date);
	public BigDecimal open(Date date);
	public BigDecimal low(Date date);
	public BigDecimal close(Date date);
	public BigDecimal adjClose(Date date);
	public BigDecimal RSI(Date date, int scale);
	public BigDecimal EMA(Date date, int scale);
	public BigDecimal SMA(Date date, int scale);
	public String currency();
	public String name();
	public String ticker();
	public String stockExchange();
	public BigDecimal currentPrice();
	public BigDecimal yearLow();
	public BigDecimal yearHigh();
	public BigDecimal volume();
	public BigDecimal averageVolume();
	public BigDecimal eps();
	public BigDecimal pe();
	public BigDecimal peg();
	public BigDecimal pb();
	public BigDecimal psales();
	public BigDecimal bvps();
	public BigDecimal shortRatio();
	public Date payableDate();
	public Date exDate();
	public BigDecimal dividendYield();
	public BigDecimal dividendYieldPercentage();
	void exportHistoryFile();
}
