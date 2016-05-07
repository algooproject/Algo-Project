package com.algotrading.stockanalysis.stockpattern;

import java.math.BigDecimal;
import java.util.Date;

import com.algotrading.stockanalysis.model.stock.IStock;

public class PatternA implements IStockPattern {

	private IStock stock;
	
	public PatternA(IStock stock) {
		this.stock = stock;
	}
	
	@Override
	public boolean buySellSignal() {
		// TODO Auto-generated method stub
		return stock.RSI(today(), 14).compareTo(new BigDecimal(30)) < 0;
	}

	private Date today() {
		// TODO Auto-generated method stub
		return null;
	}

}
