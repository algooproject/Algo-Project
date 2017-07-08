package com.algotrading.backtesting.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.algotrading.backtesting.indicatorcalculator.test.IndicatorCalculatorTestSuite;
import com.algotrading.backtesting.pattern.test.PatternTestSuite;
import com.algotrading.backtesting.patterninterperter.test.PatternInterperterTestSuite;
import com.algotrading.backtesting.portfolio.PortfolioTestSuite;
import com.algotrading.backtesting.replay.test.ReplayTestSuite;
import com.algotrading.backtesting.signal.test.SignalTestSuite;
import com.algotrading.backtesting.stockread.test.StockReadTestSuite;

@RunWith(Suite.class)
//@formatter:off
@SuiteClasses({ 
	IndicatorCalculatorTestSuite.class, 
	PatternTestSuite.class, 
	PatternInterperterTestSuite.class,
	PortfolioTestSuite.class,
	ReplayTestSuite.class,
	SignalTestSuite.class,
	StockReadTestSuite.class
	})
//@formatter:on
public class AllTests {

}