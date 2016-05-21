package com.algotrading.backtesting.patterninterperter.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.algotrading.backtesting.pattern.IStockPattern;
import com.algotrading.backtesting.patterninterperter.Expr;
import com.algotrading.backtesting.patterninterperter.Node;
import com.algotrading.backtesting.patterninterperter.ParseException;
import com.algotrading.backtesting.patterninterperter.StringContext;

public class InterperterTest {
	
	@Test
	public void test001_SeInterperter() throws ParseException {
		String expected = "SE[ 012 ]";
		Node node = new Expr();
		String input = "SE[ 012 ]";
		node.parse(new StringContext(input));
		IStockPattern pattern = node.execute();
		String actual = pattern.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void test002_andInterperter() throws ParseException {
		String expected = "AND( SE[ 012 ] , SE[ 234 ] )";
		Node node = new Expr();
		String input = "AND( SE[ 012 ] , SE[ 234 ] )";
		node.parse(new StringContext(input));
		IStockPattern pattern = node.execute();
		String actual = pattern.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void test003_orInterperter() throws ParseException {
		String expected = "OR( SE[ 012 ] , SE[ 234 ] )";
		Node node = new Expr();
		String input = "OR( SE[ 012 ] , SE[ 234 ] )";
		node.parse(new StringContext(input));
		IStockPattern pattern = node.execute();
		String actual = pattern.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void test004_notInterperter() throws ParseException {
		String expected = "NOT( SE[ 012 ] )";
		Node node = new Expr();
		String input = "NOT( SE[ 012 ] )";
		node.parse(new StringContext(input));
		IStockPattern pattern = node.execute();
		String actual = pattern.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void test005_ComplexInterperter() throws ParseException {
		String expected = "AND( OR( SE[ 123 ] , NOT( SE[ 456 ] ) ) , SE[ 012 ] )";
		Node node = new Expr();
		String input = "AND( OR( SE[ 123 ] , NOT( SE[ 456 ] ) ) , SE[ 012 ] )";
		node.parse(new StringContext(input));
		IStockPattern pattern = node.execute();
		String actual = pattern.toString();
		assertEquals(expected, actual);
	}
	
}
