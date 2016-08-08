package com.algotrading.backtesting.patterninterperter.test;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Test;

import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.patterninterperter.Expr;
import com.algotrading.backtesting.patterninterperter.Node;
import com.algotrading.backtesting.patterninterperter.StringContext;

public class InterperterTest {

	@Test
	public void test001_SeInterperter() throws ParseException {
		String expected = "SE[ 012 ]";
		String input = "SE[ 012 ]";
		StockSignal pattern = interpertToPattern(input);
		assertEquals(expected, pattern.toString());
	}

	@Test
	public void test002_andInterperter() throws ParseException {
		String expected = "AND( SE[ 012 ] , SE[ 234 ] )";
		String input = "AND( SE[ 012 ] , SE[ 234 ] )";
		StockSignal pattern = interpertToPattern(input);
		assertEquals(expected, pattern.toString());
	}

	@Test
	public void test003_orInterperter() throws ParseException {
		String expected = "OR( SE[ 012 ] , SE[ 234 ] )";
		String input = "OR( SE[ 012 ] , SE[ 234 ] )";
		StockSignal pattern = interpertToPattern(input);
		assertEquals(expected, pattern.toString());
	}

	@Test
	public void test004_notInterperter() throws ParseException {
		String expected = "NOT( SE[ 012 ] )";
		String input = "NOT( SE[ 012 ] )";
		StockSignal pattern = interpertToPattern(input);
		assertEquals(expected, pattern.toString());
	}

	@Test
	public void test005_ComplexInterperter() throws ParseException {
		String expected = "AND( OR( SE[ 123 ] , NOT( SE[ 456 ] ) ) , SE[ 012 ] )";
		String input = "AND( OR( SE[ 123 ] , NOT( SE[ 456 ] ) ) , SE[ 012 ] )";
		StockSignal pattern = interpertToPattern(input);
		assertEquals(expected, pattern.toString());
	}

	private StockSignal interpertToPattern(String input) throws ParseException {
		Node node = new Expr();
		node.parse(new StringContext(input));
		StockSignal pattern = node.execute();
		return pattern;
	}

}
