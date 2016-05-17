package com.algotrading.backtesting.patterninterperter;

import com.algotrading.backtesting.pattern.IStockPattern;

public class Interperter {
	public static void main(String[] args) throws Exception {
		Node node = new Expr();
		String a = "AND( OR( SE[ 123 ] , NOT( SE[ 456 ] ) ) , SE[ 012 ] )";
		node.parse(new StringContext(a));
		IStockPattern pattern = node.execute();
		System.out.println(pattern.toString());
	}
}

