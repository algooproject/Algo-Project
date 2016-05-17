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
	public void testBasic() throws ParseException {
		String expected = "AND( OR( SE[ 123 ] , NOT( SE[ 456 ] ) ) , SE[ 012 ] )";
		Node node = new Expr();
		String input = "AND( OR( SE[ 123 ] , NOT( SE[ 456 ] ) ) , SE[ 012 ] )";
		node.parse(new StringContext(input));
		IStockPattern pattern = node.execute();
		String actual = pattern.toString();
		assertEquals(expected, actual);
	}
	
}
