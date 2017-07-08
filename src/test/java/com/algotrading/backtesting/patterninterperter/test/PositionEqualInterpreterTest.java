package com.algotrading.backtesting.patterninterperter.test;

import java.text.ParseException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.algotrading.backtesting.pattern.PositionEqualSignal;
import com.algotrading.backtesting.patterninterperter.PositionEqualInterpreter;
import com.algotrading.backtesting.patterninterperter.StringContext;

import static org.junit.Assert.assertEquals;


public class PositionEqualInterpreterTest {

	@Test
	public void test001_basic() throws ParseException {
		StringContext context = new StringContext("PositionEqual[ expectedValueType=number expectedValue=30 multiplier=1.5 ]");
		PositionEqualInterpreter interpreter = new PositionEqualInterpreter();
		interpreter.parse(context);
		PositionEqualSignal testSignal = (PositionEqualSignal) interpreter.execute();
		PositionEqualSignal expectedSignal = new PositionEqualSignal("number", "30", 1.5 );
		
		assertEquals(expectedSignal.getExpectedValueType(), testSignal.getExpectedValueType());
		assertEquals(expectedSignal.getExpectedValue(), testSignal.getExpectedValue());
		assertEquals(expectedSignal.getMultiplier(), testSignal.getMultiplier(), 0.001);
//		assertEquals(new PositionEqualSignal("number", "30", 1.5 ), interpreter.execute());
	}

	@Test
	public void test002_default_expectedValueType() throws ParseException {
		StringContext context = new StringContext("PositionEqual[ expectedValue=30 multiplier=1.2 ]");
		PositionEqualInterpreter interpreter = new PositionEqualInterpreter();
		interpreter.parse(context);
		PositionEqualSignal testSignal = (PositionEqualSignal) interpreter.execute();
		PositionEqualSignal expectedSignal = new PositionEqualSignal("number", "30", 1.2 );		
		assertEquals(expectedSignal.getExpectedValueType(), testSignal.getExpectedValueType());
		assertEquals(expectedSignal.getExpectedValue(), testSignal.getExpectedValue());
		assertEquals(expectedSignal.getMultiplier(), testSignal.getMultiplier(), 0.001);
		
//		assertEquals(new PositionEqualSignal("number", "30", 1.2 ), interpreter.execute());
	}

	@Test
	public void test003_default_multiplier() throws ParseException {
		StringContext context = new StringContext("PositionEqual[ expectedValueType=number expectedValue=30 ]");
		PositionEqualInterpreter interpreter = new PositionEqualInterpreter();
		interpreter.parse(context);
		PositionEqualSignal testSignal = (PositionEqualSignal) interpreter.execute();
		PositionEqualSignal expectedSignal = new PositionEqualSignal("number", "30", 1 );		
		assertEquals(expectedSignal.getExpectedValueType(), testSignal.getExpectedValueType());
		assertEquals(expectedSignal.getExpectedValue(), testSignal.getExpectedValue());
		assertEquals(expectedSignal.getMultiplier(), testSignal.getMultiplier(), 0.001);
		
//		assertEquals(new PositionEqualSignal("number", "30", 1 ), interpreter.execute());
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void test004_wrongSpelling_magnitude() throws ParseException {
	   expectedEx.expect(ParseException.class);
	   expectedEx.expectMessage("PositionEqual no field match: magnituded");
	   StringContext context = new StringContext("PositionEqual[ magnituded=15 expectedValueType=variable expectedValue=closingHistory ]");
	   PositionEqualInterpreter interpreter = new PositionEqualInterpreter();
       interpreter.parse(context);
	}
	
	@Test
	public void test005_wrongSpelling_expectedValueType() throws ParseException {
	   expectedEx.expect(ParseException.class);
	   expectedEx.expectMessage("PositionEqual no field match: expectedValeuType");
	   StringContext context = new StringContext("PositionEqual[ expectedValeuType=variable expectedValue=closingHistory ]");
	   PositionEqualInterpreter interpreter = new PositionEqualInterpreter();
       interpreter.parse(context);
	}
	
}
