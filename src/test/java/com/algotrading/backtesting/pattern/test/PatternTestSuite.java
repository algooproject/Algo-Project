package com.algotrading.backtesting.pattern.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
// @formatter:off
@SuiteClasses({ CashMoreThanSignalTest.class, IsSufficientCashSignalTest.class, isStockEnabledSignalTest.class,
		RsiLowerThanSignalTest.class, SmaHigherThanSignalTest.class, RsiHigherThanSignalTest.class,
		isReentryByRsiHigherThanSignalTest.class, isExitByClosingDownToSignalTest.class })
// @formatter:on
public class PatternTestSuite {

}