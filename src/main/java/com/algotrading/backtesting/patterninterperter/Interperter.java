package com.algotrading.backtesting.patterninterperter;

import java.text.ParseException;

import com.algotrading.backtesting.pattern.StockSignal;

public class Interperter {
	public static StockSignal toPattern(String input) throws ParseException {
		Node node = new Expr();
		node.parse(new StringContext(input));
		StockSignal pattern = node.execute();
		return pattern;
	}
}
