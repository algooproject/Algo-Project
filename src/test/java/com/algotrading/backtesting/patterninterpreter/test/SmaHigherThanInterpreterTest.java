package com.algotrading.backtesting.patterninterpreter.test;

import java.text.ParseException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.algotrading.backtesting.pattern.SmaHigherThanSignal;
import com.algotrading.backtesting.patterninterpreter.SmaHigherThanInterpreter;
import com.algotrading.backtesting.patterninterpreter.StringContext;

import static org.junit.Assert.assertEquals;


public class SmaHigherThanInterpreterTest {

	@Test
	public void test001_basic() throws ParseException {
		StringContext context = new StringContext("SMAHigher[ magnitude=10 expectedValueType=number expectedValue=30 multiplier=1.5 ]");
		SmaHigherThanInterpreter interpreter = new SmaHigherThanInterpreter();
		interpreter.parse(context);
		SmaHigherThanSignal testSignal = (SmaHigherThanSignal) interpreter.execute();
		SmaHigherThanSignal expectedSignal = new SmaHigherThanSignal(10, "number", "30", 1.5 );
		assertEquals(expectedSignal.getMagnitude(), testSignal.getMagnitude());
		assertEquals(expectedSignal.getExpectedValueType(), testSignal.getExpectedValueType());
		assertEquals(expectedSignal.getExpectedValue(), testSignal.getExpectedValue());
		assertEquals(expectedSignal.getMultiplier(), testSignal.getMultiplier(), 0.001);
		
		// assertEquals(new SmaHigherThanSignal(10, "number", "30", 1.5 ), interpreter.execute());
	}

	@Test
	public void test002_default_expectedValueType() throws ParseException {
		StringContext context = new StringContext("SMAHigher[ magnitude=10 expectedValue=30 multiplier=1.2 ]");
		SmaHigherThanInterpreter interpreter = new SmaHigherThanInterpreter();
		interpreter.parse(context);
		SmaHigherThanSignal testSignal = (SmaHigherThanSignal) interpreter.execute();
		SmaHigherThanSignal expectedSignal = new SmaHigherThanSignal(10, "number", "30", 1.2 );
		assertEquals(expectedSignal.getMagnitude(), testSignal.getMagnitude());
		assertEquals(expectedSignal.getExpectedValueType(), testSignal.getExpectedValueType());
		assertEquals(expectedSignal.getExpectedValue(), testSignal.getExpectedValue());
		assertEquals(expectedSignal.getMultiplier(), testSignal.getMultiplier(), 0.001);
		// assertEquals(new SmaHigherThanSignal(10, "number", "30", 1.2 ), interpreter.execute());
	}

	@Test
	public void test003_default_multiplier() throws ParseException {
		StringContext context = new StringContext("SMAHigher[ magnitude=15 expectedValueType=number expectedValue=30 ]");
		SmaHigherThanInterpreter interpreter = new SmaHigherThanInterpreter();
		interpreter.parse(context);
		SmaHigherThanSignal testSignal = (SmaHigherThanSignal) interpreter.execute();
		SmaHigherThanSignal expectedSignal = new SmaHigherThanSignal(15, "number", "30", 1 );
		assertEquals(expectedSignal.getMagnitude(), testSignal.getMagnitude());
		assertEquals(expectedSignal.getExpectedValueType(), testSignal.getExpectedValueType());
		assertEquals(expectedSignal.getExpectedValue(), testSignal.getExpectedValue());
		assertEquals(expectedSignal.getMultiplier(), testSignal.getMultiplier(), 0.001);
		// assertEquals(new SmaHigherThanSignal(15, "number", "30", 1 ), interpreter.execute());
	}

	@Test
	public void test004_variable_closingHistory() throws ParseException {
		StringContext context = new StringContext("SMAHigher[ magnitude=15 expectedValueType=variable expectedValue=closingHistory ]");
		SmaHigherThanInterpreter interpreter = new SmaHigherThanInterpreter();
		interpreter.parse(context);
		SmaHigherThanSignal testSignal = (SmaHigherThanSignal) interpreter.execute();
		SmaHigherThanSignal expectedSignal = new SmaHigherThanSignal(15, "variable", "closingHistory", 1 );
		assertEquals(expectedSignal.getMagnitude(), testSignal.getMagnitude());
		assertEquals(expectedSignal.getExpectedValueType(), testSignal.getExpectedValueType());
		assertEquals(expectedSignal.getExpectedValue(), testSignal.getExpectedValue());
		assertEquals(expectedSignal.getMultiplier(), testSignal.getMultiplier(), 0.001);
		// assertEquals(new SmaHigherThanSignal(15, "variable", "closing", 1 ), interpreter.execute());
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void test005_wrongSpelling_magnitude() throws ParseException {
	   expectedEx.expect(ParseException.class);
	   expectedEx.expectMessage("SMAHigher no field match: magnituded");
	   StringContext context = new StringContext("SMAHigher[ magnituded=15 expectedValueType=variable expectedValue=closingHistory ]");
	   SmaHigherThanInterpreter interpreter = new SmaHigherThanInterpreter();
	   interpreter.parse(context);
	}
	
	@Test
	public void test006_wrongSpelling_expectedValueType() throws ParseException {
	   expectedEx.expect(ParseException.class);
	   expectedEx.expectMessage("SMAHigher no field match: expectedValeuType");
       StringContext context = new StringContext("SMAHigher[ magnitude=15 expectedValeuType=variable expectedValue=closingHistory ]");
       SmaHigherThanInterpreter interpreter = new SmaHigherThanInterpreter();
	   interpreter.parse(context);
	}
	
}
